package zornco.reploidcraftenv.entities;

import static zornco.reploidcraftenv.entities.armorParts.PartSlot.ARMLEFT;
import static zornco.reploidcraftenv.entities.armorParts.PartSlot.ARMRIGHT;
import static zornco.reploidcraftenv.entities.armorParts.PartSlot.BACK;
import static zornco.reploidcraftenv.entities.armorParts.PartSlot.BODY;
import static zornco.reploidcraftenv.entities.armorParts.PartSlot.HEAD;
import static zornco.reploidcraftenv.entities.armorParts.PartSlot.LEGS;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import zornco.reploidcraftenv.ReploidCraftEnv;
import zornco.reploidcraftenv.entities.armorParts.IPartArm;
import zornco.reploidcraftenv.entities.armorParts.IPartBack;
import zornco.reploidcraftenv.entities.armorParts.IPartBody;
import zornco.reploidcraftenv.entities.armorParts.IPartChest;
import zornco.reploidcraftenv.entities.armorParts.IPartHead;
import zornco.reploidcraftenv.entities.armorParts.IPartLegs;
import zornco.reploidcraftenv.entities.armorParts.PartSlot;
import zornco.reploidcraftenv.utils.RiderState;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityRideArmor extends EntityCreature implements IInvBasic, IEntityMultiPart
{

	public float prevSitAngle;
	public float sitAngle;
	public EntityRideArmorPart[] rideArmorParts;
	/** The head bounding box of the Ride Armor */
	public EntityRideArmorPart rideArmorHead;
	/** The body bounding box of Ride Armor */
	public EntityRideArmorPart rideArmorBody;
	public EntityRideArmorPart rideArmorBack;
	public EntityRideArmorPart rideArmorFeet;
	public EntityRideArmorPart rideArmorArmLeft;
	public EntityRideArmorPart rideArmorArmRight;
	public RiderState riderState;

	private static final IAttribute mechJumpStrength = (new RangedAttribute("mech.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(
			true);
	// private static final String[] mechArmorTextures = new String[] {null,
	// "textures/entity/mech/armor/mech_armor_iron.png",
	// "textures/entity/mech/armor/mech_armor_gold.png",
	// "textures/entity/mech/armor/mech_armor_diamond.png"};
	// private static final String[] mechTextures = new String[]
	// {"textures/entity/mech/mech_white.png",
	// "textures/entity/mech/mech_creamy.png",
	// "textures/entity/mech/mech_chestnut.png",
	// "textures/entity/mech/mech_brown.png",
	// "textures/entity/mech/mech_black.png",
	// "textures/entity/mech/mech_gray.png",
	// "textures/entity/mech/mech_darkbrown.png"};

	public int field_110278_bp;
	public int field_110279_bq;
	protected boolean mechJumping;
	private AnimalChest mechChest;
	protected float jumpPower;

	private String lastPartsString = "";

	/*
	 * SECTION: Init
	 */
	public EntityRideArmor(World par1World)
	{
		super(par1World);
		this.setSize(1.4F, 2.5F);
		this.isImmuneToFire = false;
		this.setChested(false);
		// this.getNavigator().setAvoidsWater(true);
		// this.tasks.addTask(0, new EntityAISwimming(this));
		// this.tasks.addTask(1, new EntityAIPanic(this, 1.2D));
		// this.tasks.addTask(6, new EntityAIWander(this, 0.7D));
		// this.tasks.addTask(7, new EntityAIWatchClosest(this,
		// EntityPlayer.class, 6.0F));
		// this.tasks.addTask(8, new EntityAILookIdle(this));
		this.chestSetup();
		this.riderState = new RiderState();
		this.rideArmorParts = new EntityRideArmorPart[] { rideArmorHead = new EntityRideArmorPart(this, "EMPTY", HEAD),
				rideArmorBody = new EntityRideArmorPart(this, "EMPTY", BODY), rideArmorBack = new EntityRideArmorPart(this, "EMPTY", BACK),
				rideArmorFeet = new EntityRideArmorPart(this, "EMPTY", LEGS), rideArmorArmLeft = new EntityRideArmorPart(this, "EMPTY", ARMLEFT),
				rideArmorArmRight = new EntityRideArmorPart(this, "EMPTY", ARMRIGHT) };

		setStepHeight(1.0F);
	}

	public EntityRideArmor(World par1World, double par2, double par4, double par6)
	{
		this(par1World);
		this.setPosition(par2, par4 + (double) this.yOffset, par6);
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		this.prevPosX = par2;
		this.prevPosY = par4;
		this.prevPosZ = par6;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
		this.dataWatcher.addObject(21, String.valueOf(""));
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(mechJumpStrength);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
	}

	public void setRiderState(RiderState riderState)
	{
		this.riderState.setMoveStrafe(riderState.getMoveStrafe());
		this.riderState.setMoveForward(riderState.getMoveForward());
		this.riderState.setJump(riderState.isJump());
		this.riderState.setSneak(riderState.isSneak());
	}

	/*
	 * SECTION: Watch Objects
	 */
	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	public String getCommandSenderName()
	{
		if (this.hasCustomNameTag())
		{
			return this.getCustomNameTag();
		}
		else
		{
			return StatCollector.translateToLocal("entity.mech.name");
		}
	}

	private boolean getMechWatchableBoolean(int par1)
	{
		return (this.dataWatcher.getWatchableObjectInt(16) & par1) != 0;
	}

	private void setMechWatchableBoolean(int par1, boolean par2)
	{
		int j = this.dataWatcher.getWatchableObjectInt(16);

		if (par2)
		{
			this.dataWatcher.updateObject(16, Integer.valueOf(j | par1));
		}
		else
		{
			this.dataWatcher.updateObject(16, Integer.valueOf(j & ~par1));
		}
	}

	public boolean isJumping()
	{
		return this.getMechWatchableBoolean(64);
	}

	public void setJumping(boolean par1)
	{
		super.setJumping(par1);
		this.setMechWatchableBoolean(64, par1);
	}

	public void setChested(boolean par1)
	{
		this.setMechWatchableBoolean(8, par1);
	}

	public boolean isChested()
	{
		return this.getMechWatchableBoolean(8);
	}

	/**
	 * Sets the parts of the entity.
	 */
	public void setPartsString()
	{
		String s = "";
		if (this.rideArmorParts != null)
		{
			for (int i = 0; i < rideArmorParts.length; i++)
			{

				s = s + partSwitch(i).getType() + ":";
			}
		}
		this.dataWatcher.updateObject(21, String.valueOf(s));
	}

	/**
	 * Gets the parts of the entity.
	 */
	public String getPartsString()
	{
		String s = this.dataWatcher.getWatchableObjectString(21);
		if (!s.equals(""))
		{
			String[] sa = s.split(":");
			for (int i = 0; i < sa.length; i++)
			{
				partSwitch(i).setType(sa[i]);
			}
		}
		return s;
	}

	/*
	 * SECTION: Spawn
	 */
	public boolean prepareChunkForSpawn()
	{
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posZ);
		this.worldObj.getBiomeGenForCoords(i, j);
		return true;
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere()
	{
		this.prepareChunkForSpawn();
		return super.getCanSpawnHere();
	}

	/*
	 * SECTION: Sounds
	 */

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound()
	{
		return "mob.horse.idle";
	}

	/**
	 * Returns the sound this mob makes when it jumps.
	 */
	protected String getJumpSound()
	{
		return "mob.mech.jump";
	}

	/**
	 * Returns the sound this mob makes when it lands.
	 */
	protected String getLandSound()
	{
		return "mob.mech.land";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound()
	{
		return "mob.mech.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound()
	{
		return "mob.mech.death";
	}

	/**
	 * Walk sounds
	 */
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
	{
		/*
		 * Block.SoundType soundtype = p_145780_4_.stepSound;
		 * 
		 * if (this.worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_)
		 * == Blocks.snow_layer) { soundtype = Blocks.snow_layer.stepSound; }
		 * 
		 * if (!p_145780_4_.getMaterial().isLiquid()) { int l =
		 * this.getMechType();
		 * 
		 * if (this.riddenByEntity != null && l != 1 && l != 2) {
		 * ++this.field_110285_bP;
		 * 
		 * if (this.field_110285_bP > 5 && this.field_110285_bP % 3 == 0) {
		 * this.playSound("mob.mech.gallop", soundtype.getVolume() * 0.15F,
		 * soundtype.getPitch());
		 * 
		 * if (l == 0 && this.rand.nextInt(10) == 0) {
		 * this.playSound("mob.mech.breathe", soundtype.getVolume() * 0.6F,
		 * soundtype.getPitch()); } } else if (this.field_110285_bP <= 5) {
		 * this.playSound("mob.mech.wood", soundtype.getVolume() * 0.15F,
		 * soundtype.getPitch()); } } else if (soundtype == Block.soundTypeWood)
		 * { this.playSound("mob.mech.wood", soundtype.getVolume() * 0.15F,
		 * soundtype.getPitch()); } else { this.playSound("mob.mech.soft",
		 * soundtype.getVolume() * 0.15F, soundtype.getPitch()); } }
		 */
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume()
	{
		return 0.8F;
	}

	/**
	 * Get number of ticks, at least during which the living entity will be
	 * silent.
	 */
	public int getTalkInterval()
	{
		return 400;
	}

	/*
	 * SECTION: Interact
	 */

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer)
	{
		setPartsString();
		ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

		if (par1EntityPlayer.isSneaking())
		{
			this.openGUI(par1EntityPlayer);
			return true;
		}
		else if (this.riddenByEntity != null)
		{
			return super.interact(par1EntityPlayer);
		}
		else
		{
			if (itemstack != null)
			{
				boolean flag = false;

				float healAmount = 0.0F;

				if (itemstack.getItem() == Items.wheat)
				{
					healAmount = 2.0F;
				}
				else if (itemstack.getItem() == Items.sugar)
				{
					healAmount = 1.0F;
				}
				else if (itemstack.getItem() == Items.bread)
				{
					healAmount = 7.0F;
				}
				else if (Block.getBlockFromItem(itemstack.getItem()) == Blocks.hay_block)
				{
					healAmount = 20.0F;
				}
				else if (itemstack.getItem() == Items.apple)
				{
					healAmount = 3.0F;
				}

				if (this.getHealth() < this.getMaxHealth() && healAmount > 0.0F)
				{
					this.heal(healAmount);
					flag = true;
				}

				if (!this.isChested() && itemstack.getItem() == Item.getItemFromBlock(Blocks.chest))
				{
					this.setChested(true);
					this.playSound("mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					flag = true;
					this.chestSetup();
				}

				if (flag)
				{
					if (!par1EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize == 0)
					{
						par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack) null);
					}

					return true;
				}
			}

			if (this.riddenByEntity == null)
			{
				if (itemstack != null && itemstack.interactWithEntity(par1EntityPlayer, this))
				{
					return true;
				}
				else
				{
					this.mountPlayer(par1EntityPlayer);
					return true;
				}
			}
			else
			{
				return super.interact(par1EntityPlayer);
			}
		}
	}

	public void openGUI(EntityPlayer par1EntityPlayer)
	{
		if (!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == par1EntityPlayer))
		{
			this.mechChest.func_110133_a(this.getCommandSenderName());
			// par1EntityPlayer.displayGUIMech(this, this.mechChest);
		}
	}

	private void mountPlayer(EntityPlayer par1EntityPlayer)
	{
		par1EntityPlayer.rotationYaw = this.rotationYaw;
		par1EntityPlayer.rotationPitch = this.rotationPitch;
		this.setJumping(false);

		if (!this.worldObj.isRemote)
		{
			par1EntityPlayer.mountEntity(this);
		}
	}

	@Override
	public boolean canRiderInteract()
	{
		return true;
	}

	/*
	 * SECTION: Updaters
	 */

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate()
	{
		super.onUpdate();
		// TODO: setdead
		// this.setDead();

		if (isMechFireResistant())
			this.fireResistance = 1;
		if (this.fallDistance > 2)
			this.fallDistance = 2.0F;
		if (this.isJumping() && this.onGround)
		{
			this.makeMechLandWithSound();
			// System.out.println("I'm jumping!");

		}
		if (!this.onGround && !this.mechJumping) this.mechJumping = true;
		
		if (this.worldObj.isRemote && this.onGround && this.mechJumping)
		{
			this.mechJumping = false;
			this.doMechParticle(this, BODY);
		}
		if (this.field_110279_bq > 0)
		{
			++this.field_110279_bq;

			if (this.field_110279_bq > 300)
			{
				this.field_110279_bq = 0;
			}
		}
		if (riddenByEntity != null && canMechWaterBreathe())
		{
			riddenByEntity.setAir(300);
		}
		
		if (this.riddenByEntity != null)
			ReploidCraftEnv.proxy.updateRiderState(this, this.riddenByEntity);
		this.updateParts();
		
		this.prevSitAngle = this.sitAngle;
		float sitStep = 0.1F;

		if ((this.riddenByEntity != null && this.sitAngle > 0.0F) || (this.riddenByEntity == null && this.sitAngle < 1.0F))
		{
			float f1 = this.sitAngle;

			if (this.riddenByEntity == null)
			{
				this.sitAngle += sitStep;
			}
			else
			{
				this.sitAngle -= sitStep;
			}

			if (this.sitAngle > 1.0F)
			{
				this.sitAngle = 1.0F;
			}

			if (this.sitAngle < 0.0F)
			{
				this.sitAngle = 0.0F;
			}

			float f2 = 0.5F;

			if (this.sitAngle < f2 && f1 >= f2)
			{
				double d5 = (double) this.posX + 0.5D;
				double d6 = (double) this.posZ + 0.5D;

				this.worldObj.playSoundEffect(d5, (double) this.posY + 0.5D, d6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
			}
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.updatePartsString();

		if (!this.worldObj.isRemote)
		{
			if (this.rand.nextInt(900) == 0 && this.deathTime == 0)
			{
				this.heal(1.0F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte par1)
	{
		if (par1 == 7)
		{
			this.spawnMechParticles(true);
		}
		else if (par1 == 6)
		{
			this.spawnMechParticles(false);
		}
		else
		{
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource)
	{
		super.onDeath(par1DamageSource);

		if (!this.worldObj.isRemote)
		{
			this.dropChestItems();
		}
	}

	/*
	 * SECTION: Movement
	 */

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	public void moveEntityWithHeading(float par1, float par2)
	{
		if (this.riddenByEntity != null)
		{
			if (this.hasPart(LEGS))
			{
				this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
				this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
				this.setRotation(this.rotationYaw, this.rotationPitch);
				this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
				par1 = ((EntityLivingBase) this.riddenByEntity).moveStrafing * 0.5F;
				par2 = ((EntityLivingBase) this.riddenByEntity).moveForward;

				if (par2 <= 0.0F)
				{
					par2 *= 0.5F;
				}
				
				if ((this.riddenByEntity instanceof EntityPlayer))
				{
					if (this.riderState.isJump() && !this.isJumping() && this.onGround)
					{

						this.motionY = this.getMechJumpStrength();
						this.makeMechJumpWithSound();
						this.isAirBorne = true;
						doMechParticle(this, BACK);
						this.playSound("mob.mech.jump", 0.4F, 1.0F);
					}
				}
				setStepHeight(1.0F);
				this.setAIMoveSpeed(getMechSpeed());
				super.moveEntityWithHeading(par1, par2);
				
				this.prevLimbSwingAmount = this.limbSwingAmount;
				double d1 = this.posX - this.prevPosX;
				double d0 = this.posZ - this.prevPosZ;
				float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

				if (f4 > 1.0F)
				{
					f4 = 1.0F;
				}

				this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
				this.limbSwing += this.limbSwingAmount;
				

			}
		}
		else
		{
			setStepHeight(0.5F);
			super.moveEntityWithHeading(0.0F, 0.0F);
		}
	}

	@Override
	public void applyEntityCollision(Entity p_70108_1_)
	{
		if (!this.worldObj.isRemote)
		{
			if (p_70108_1_ != this.riddenByEntity)
			{
				if (p_70108_1_ instanceof EntityLivingBase //
						&& !(p_70108_1_ instanceof EntityRideArmor || p_70108_1_ instanceof EntityRideArmorPart) //
						&& !(p_70108_1_ instanceof EntityPlayer) && !(p_70108_1_ instanceof EntityIronGolem) //
						&& !(p_70108_1_ instanceof EntityMob && !(p_70108_1_ instanceof EntityZombie) && !(p_70108_1_ instanceof EntitySkeleton)) //
						&& !(p_70108_1_ instanceof EntityAgeable && !(p_70108_1_ instanceof EntityVillager))//
						&& this.riddenByEntity == null && p_70108_1_.ridingEntity == null)
				{
					p_70108_1_.mountEntity(this);
				}
			}
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1)
	{
		if (par1 > 1.0F)
		{
			this.playSound("mob.mech.land", 0.4F, 1.0F);
		}

		int i = MathHelper.ceiling_float_int(par1 * 0.5F - 3.0F);

		if (i > 0)
		{
			this.attackEntityFrom(DamageSource.fall, (float) i);

			if (this.riddenByEntity != null)
			{
				this.riddenByEntity.attackEntityFrom(DamageSource.fall, (float) i);
			}

			Block block = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.2D - (double) this.prevRotationYaw),
					MathHelper.floor_double(this.posZ));

			if (block.getMaterial() != Material.air)
			{
				Block.SoundType soundtype = block.stepSound;
				this.worldObj.playSoundAtEntity(this, soundtype.getStepResourcePath(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
			}
		}
	}

	public void makeMechJumpWithSound()
	{
		this.setJumping(true);
		
		String s = this.getJumpSound();

		if (s != null)
		{
			this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
		}

	}

	public void makeMechLandWithSound()
	{
		this.setJumping(false);
		
		String s = this.getJumpSound();

		if (s != null)
		{
			this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
		}

	}

	public void setStepHeight(float step)
	{
		this.stepHeight = this.hasPart(LEGS) ? step : 0.0F;
	}

	public void updateRiderPosition()
	{
		super.updateRiderPosition();

		float f = MathHelper.sin(this.renderYawOffset * (float) Math.PI / 180.0F);
		float f1 = MathHelper.cos(this.renderYawOffset * (float) Math.PI / 180.0F);
		float f2 = 0.2F;
		
		this.riddenByEntity.setPosition(this.posX + (double) (f2 * f), this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ - (double) (f2 * f1));

		if (this.riddenByEntity instanceof EntityLivingBase)
		{
			((EntityLivingBase) this.riddenByEntity).renderYawOffset = this.renderYawOffset;
		}
	}

	@Override
	public double getMountedYOffset()
	{
		return (double) this.height * 0.0D + 2.125D + getSitOffset();
	}

	public float getSitOffset()
	{
		return (float) (Math.sin((double) (this.sitAngle * (float) Math.PI + 0.75F))) * 0.7375F - 0.5F;
	}

	/**
	 * Dead and sleeping entities cannot move
	 */
	protected boolean isMovementBlocked()
	{
		return this.riddenByEntity == null && this.sitAngle == 1.0F && this.hasPart(LEGS);
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	public boolean canBePushed()
	{
		return this.riddenByEntity == null && this.hasPart(LEGS);
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	public boolean isOnLadder()
	{
		return false;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled()
	{
		return true;
	}

	/*
	 * SECTION: Part stuff
	 */
	@Override
	public Entity[] getParts()
	{
		return this.rideArmorParts;
	}

	/**
	 * getWorld for IEntityMultiPart
	 */
	@Override
	public World func_82194_d()
	{
		return this.worldObj;
	}

	public void setPart(PartSlot slot, String type)
	{
		this.partSwitch(slot.ordinal()).setType(type);
		this.updatePartsString();
	}

	public boolean hasPart(PartSlot part)
	{
		return !this.partSwitch(part.ordinal()).getType().equals("EMPTY");
	}

	public boolean shouldDismountInWater(Entity rider)
	{
		return canMechWaterBreathe();
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart var1, DamageSource var2, float var3)
	{
		EntityRideArmorPart part = (EntityRideArmorPart) var1;
		if (part.getHealth() <= 0.0F)
		{
			return false;
		}
		if ((float) part.hurtResistantTime > (float) part.maxHurtResistantTime / 2.0F)
		{
			if (var3 <= part.lastDamage)
			{
				return false;
			}

			part.damageEntity(var2, var3 - part.lastDamage);
			part.lastDamage = var3;
		}
		else
		{
			part.lastDamage = var3;
			part.prevHealth = part.getHealth();
			part.hurtResistantTime = part.maxHurtResistantTime;
			part.damageEntity(var2, var3);
			part.hurtTime = this.maxHurtTime = 10;
		}
		// System.out.println("hit on the " + part.getName() + FMLCommonHandler.instance().getEffectiveSide());

		//doesn't seem like this is working
		if (part.getHealth() <= 0.0F)
		{
			if (part.getName() == ARMLEFT && hasPart(ARMLEFT))
			{
				this.rideArmorArmLeft.setType("EMPTY");
				return true;
			}
			if (part.getName() == ARMRIGHT && hasPart(ARMRIGHT))
			{
				this.rideArmorArmRight.setType("EMPTY");
				return true;
			}
			if (part.getName() == BACK && hasPart(BACK))
			{
				this.rideArmorBack.setType("EMPTY");
				return true;
			}
			if (part.getName() == HEAD)
			{
				if (this.riddenByEntity != null && var2.getEntity() != this.riddenByEntity)
				{
					this.riddenByEntity.attackEntityFrom(var2, var3);
					return true;
				}
				else if (var2.getEntity() == this.riddenByEntity)
				{
					doMechAttackLeft();
					return true;
				}
			}

			if (part.getName() == BODY)
			{
				if (this.riddenByEntity != null && var2.getEntity() == this.riddenByEntity)
				{
					doMechAttackLeft();
					return true;
				}
				return this.attackEntityFrom2(var2, var3);
			}

			if (part.getName() == LEGS)
			{
				// effect legs
				if (this.ridingEntity != null) //mech may be in a cart?
				{
					this.ridingEntity.attackEntityFrom(var2, var3);
				}
				this.rideArmorFeet.setType("EMPTY");
				//return this.attackEntityFrom2(var2, var3);
			}
		}

		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		return true;
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom2(DamageSource par1DamageSource, float par2)
	{
		return super.attackEntityFrom(par1DamageSource, par2);
	}

	protected void updateParts()
	{
		float f3 = this.rotationYaw * (float) Math.PI / 180.0F;
		if (rideArmorParts != null)
		{
			for (int i = 0; i < rideArmorParts.length; i++)
			{
				updatePart(partSwitch(i), f3);
			}

		}
	}

	private void updatePart(EntityRideArmorPart part, float rotation)
	{
		float f1 = MathHelper.sin(rotation);
		float f2 = MathHelper.cos(rotation);
		float[] offsets = part.getOffsetsPos();
		if (this.hasPart(ARMRIGHT) && part.slot == ARMRIGHT && ReploidCraftEnv.proxy.partRegistry.getPart(part.getType(), part.slot) instanceof IPartArm)
		{
			if (((IPartArm) ReploidCraftEnv.proxy.partRegistry.getPart(part.getType(), part.slot)).isMirrored())
				offsets = ((IPartArm) ReploidCraftEnv.proxy.partRegistry.getPart(part.getType(), part.slot)).getArmPos(ARMRIGHT);
		}

		double X = this.posX + offsets[0] * f2 + offsets[2] * f1;
		double Z = this.posZ + offsets[0] * f1 - offsets[2] * f2;
		part.onUpdate();
		part.setLocationAndAngles(X, this.posY + offsets[1], Z, 0.0F, 0.0F);
	}

	public EntityRideArmorPart partSwitch(int index)
	{
		if (index == HEAD.ordinal())
		{
			return rideArmorHead;
		}
		else if (index == BODY.ordinal())
		{
			return rideArmorBody;
		}
		else if (index == BACK.ordinal())
		{
			return rideArmorBack;
		}
		else if (index == LEGS.ordinal())
		{
			return rideArmorFeet;
		}
		else if (index == ARMLEFT.ordinal())
		{
			return rideArmorArmLeft;
		}
		else if (index == ARMRIGHT.ordinal())
		{
			return rideArmorArmRight;
		}
		else return null;

	}

	private void updatePartsString()
	{
		if (!this.worldObj.isRemote)
		{
			this.setPartsString();
		}
		String s = this.getPartsString();

		if (!lastPartsString.equals(s))
		{
			lastPartsString = s;
		}
	}

	/**
	 * "Spawns particles for the mech entity. par1 tells whether to spawn hearts. If it is false, it spawns smoke."
	 */
	@SideOnly(Side.CLIENT)
	protected void spawnMechParticles(boolean par1)
	{
		String s = par1 ? "heart" : "smoke";

		for (int i = 0; i < 7; ++i)
		{
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(s, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width,
					this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height),
					this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
		}
	}

	public double getMechJumpStrength()
	{
		if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(BACK.ordinal()).getType(), BACK) instanceof IPartBack)
		{
			return ((IPartBack) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(BACK.ordinal()).getType(), BACK)).getJumpHeight();
		}
		return 0.4F;
	}

	public double getMechJumpDuration()
	{
		if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(BACK.ordinal()).getType(), BACK) instanceof IPartBack)
		{
			return ((IPartBack) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(BACK.ordinal()).getType(), BACK)).getJumpDuration();
		}
		return 0.0F;
	}

	public boolean doesMechHaveChest(PartSlot slot)
	{
		return ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(slot.ordinal()).getType(), slot) instanceof IPartChest;
	}

	public boolean doMechAttackLeft()
	{
		if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(ARMLEFT.ordinal()).getType(), ARMLEFT) instanceof IPartArm)
		{
			return ((IPartArm) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(ARMLEFT.ordinal()).getType(), ARMLEFT)).doAttack(this);
		}
		return false;
	}

	public boolean doMechAttackRight(Entity par1)
	{
		if (par1 == this.riddenByEntity)
			if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(ARMRIGHT.ordinal()).getType(), ARMRIGHT) instanceof IPartArm)
			{
				return ((IPartArm) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(ARMRIGHT.ordinal()).getType(), ARMRIGHT)).doAttack(this);
			}
		return false;
	}

	public void doMechParticle(Entity mech, PartSlot slot)
	{
		ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(slot.ordinal()).getType(), slot).doParticle(mech);
	}

	public float getMechSpeed()
	{
		if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(LEGS.ordinal()).getType(), LEGS) instanceof IPartLegs)
		{
			return ((IPartLegs) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(LEGS.ordinal()).getType(), LEGS)).getSpeed();
		}
		return 0.2F;
	}

	public boolean canMechWaterBreathe()
	{
		if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(HEAD.ordinal()).getType(), HEAD) instanceof IPartHead)
		{
			return ((IPartHead) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(HEAD.ordinal()).getType(), HEAD)).isSealed();
		}
		return false;
	}

	public boolean isMechFireResistant()
	{
		if (ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(BODY.ordinal()).getType(), BODY) instanceof IPartBody)
		{
			return ((IPartBody) ReploidCraftEnv.proxy.partRegistry.getPart(this.partSwitch(BODY.ordinal()).getType(), BODY)).isLavaResistant();
		}
		return false;
	}

	/*
	 * SECTION: Inventory
	 */

	/**
	 * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
	 */
	public void onInventoryChanged(InventoryBasic par1InventoryBasic)
	{
		/*
		 * int i = this.getArmor(); this.func_110232_cE();
		 * 
		 * if (this.ticksExisted > 20) { if (i == 0 && i != this.getArmor()) {
		 * this.playSound("mob.mech.armor", 0.5F, 1.0F); } }
		 */
	}

	public void dropChests()
	{
		if (!this.worldObj.isRemote && this.isChested())
		{
			this.dropItem(Item.getItemFromBlock(Blocks.chest), 1);
			this.setChested(false);
		}
	}

	private int canBeCHest()
	{
		return this.isChested() ? 17 : 2;
	}

	private void chestSetup()
	{
		AnimalChest animalchest = this.mechChest;
		this.mechChest = new AnimalChest("MechChest", this.canBeCHest());
		this.mechChest.func_110133_a(this.getCommandSenderName());

		if (animalchest != null)
		{
			animalchest.func_110132_b(this);
			int i = Math.min(animalchest.getSizeInventory(), this.mechChest.getSizeInventory());

			for (int j = 0; j < i; ++j)
			{
				ItemStack itemstack = animalchest.getStackInSlot(j);

				if (itemstack != null)
				{
					this.mechChest.setInventorySlotContents(j, itemstack.copy());
				}
			}

			animalchest = null;
		}

		this.mechChest.func_110134_a(this);
		this.updatePartsString();
	}

	protected Item getDropItem()
	{
		return ReploidCraftEnv.rideArmorPlacer;
	}

	public ItemStack[] populateInventory()
	{
		ItemStack[] inv = new ItemStack[6];
		for (int i = 0; i < getParts().length; i++)
		{
			if(((EntityRideArmorPart) this.getParts()[i]).getType() != "EMPTY" && ReploidCraftEnv.proxy.partRegistry.getPart(((EntityRideArmorPart) this.getParts()[i]).getType(), PartSlot.getSlot(i)) != null)
				inv[i] = new ItemStack(ReploidCraftEnv.rideArmorPart, 1, ReploidCraftEnv.proxy.partRegistry.getPart(((EntityRideArmorPart) this.getParts()[i]).getType(), PartSlot.getSlot(i)).getPartNumber());
		}
		return inv;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2)
	{
		//Item item = this.getDropItem();
		ItemStack[] inv = populateInventory();
		for (ItemStack item : inv)
		{
			if (item.getItem() != null)
			{
				this.dropItem(item.getItem(), rand.nextInt(1));
			}
		}
	}

	public void dropChestItems()
	{
		this.dropItemsInChest(this, this.mechChest);
		this.dropChests();
	}

	private void dropItemsInChest(Entity par1Entity, AnimalChest par2AnimalChest)
	{
		if (par2AnimalChest != null && !this.worldObj.isRemote)
		{
			for (int i = 0; i < par2AnimalChest.getSizeInventory(); ++i)
			{
				ItemStack itemstack = par2AnimalChest.getStackInSlot(i);

				if (itemstack != null)
				{
					this.entityDropItem(itemstack, 0.0F);
				}
			}
		}
	}

	/*
	 * SECTION: Save/Load
	 */
	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("ChestedMech", this.isChested());

		if (this.isChested())
		{
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 2; i < this.mechChest.getSizeInventory(); ++i)
			{
				ItemStack itemstack = this.mechChest.getStackInSlot(i);

				if (itemstack != null)
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte) i);
					itemstack.writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}

			par1NBTTagCompound.setTag("Items", nbttaglist);
		}

		if (this.mechChest.getStackInSlot(0) != null)
		{
			par1NBTTagCompound.setTag("SaddleItem", this.mechChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
		}
		if (this.rideArmorParts != null)
		{
			NBTTagList nbttaglist = new NBTTagList();
			NBTTagCompound nbttagcompound1;

			for (int i = 0; i < this.rideArmorParts.length; ++i)
			{
				nbttagcompound1 = new NBTTagCompound();

				if (partSwitch(i) != null)
				{
					partSwitch(i).writeToNBT(nbttagcompound1);
				}

				nbttaglist.appendTag(nbttagcompound1);
			}
			par1NBTTagCompound.setTag("Parts", nbttaglist);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setChested(par1NBTTagCompound.getBoolean("ChestedMech"));

		IAttributeInstance iattributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed");

		if (iattributeinstance != null)
		{
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
		}

		if (this.isChested())
		{
			NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
			this.chestSetup();

			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				int j = nbttagcompound1.getByte("Slot") & 255;

				if (j >= 2 && j < this.mechChest.getSizeInventory())
				{
					this.mechChest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound1));
				}
			}
		}

		ItemStack itemstack;

		if (par1NBTTagCompound.hasKey("SaddleItem", 10))
		{
			itemstack = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("SaddleItem"));

			if (itemstack != null && itemstack.getItem() == Items.saddle)
			{
				this.mechChest.setInventorySlotContents(0, itemstack);
			}
		}
		else if (par1NBTTagCompound.getBoolean("Saddle"))
		{
			this.mechChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
		}
		NBTTagList nbttaglist;
		if (par1NBTTagCompound.hasKey("Parts", 9))
		{
			nbttaglist = par1NBTTagCompound.getTagList("Parts", 10);
			for (int i = 0; i < this.rideArmorParts.length; ++i)
			{
				EntityRideArmorPart part = this.loadPartFromNBT(nbttaglist.getCompoundTagAt(i), PartSlot.values()[i]);
				if (part != null)
				{
					// I forgot what I was doing here
					if (partSwitch(i).getType().equals("EMPTY"))
					{
						partSwitch(i).copyDataFrom(part, true);
					}
					else
					{
						partSwitch(i).copyDataFrom(part, true);
					}
				}
				else
				{
					System.out.println("read broke on " + PartSlot.values()[i]);
				}
			}
		}

		this.updatePartsString();
	}

	public EntityRideArmorPart loadPartFromNBT(NBTTagCompound par0NBTTagCompound, PartSlot partName)
	{
		EntityRideArmorPart part = new EntityRideArmorPart(this, partName);
		part.readFromNBT(par0NBTTagCompound);
		return part.getType().toString() != "" ? part : null;
	}

}
