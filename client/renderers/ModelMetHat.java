// Date: 10/2/2012 4:09:08 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package zornco.reploidcraftenv.client.renderers;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import zornco.reploidcraftenv.entities.EntityMet;
public class ModelMetHat extends ModelBase
{
	//fields
	ModelRenderer Top;
	ModelRenderer Front;
	ModelRenderer Right;
	ModelRenderer Back;
	ModelRenderer Left;
	ModelRenderer Corner1;
	ModelRenderer Corner2;
	ModelRenderer Corner3;
	ModelRenderer Corner4;
	ModelRenderer Lip1;
	ModelRenderer Lip3;
	ModelRenderer Lip4;
	ModelRenderer Lip2;

	public ModelMetHat()
	{
		textureWidth = 64;
		textureHeight = 32;

		Top = new ModelRenderer(this, 16, 0);
		Top.addBox(-3.5F, -3.5F, -3F, 7, 7, 1);
		Top.setRotationPoint(0F, 18F, 0F);
		Top.setTextureSize(64, 32);
		Top.mirror = true;
		setRotation(Top, -1.570796F, 0F, 0F);
		Front = new ModelRenderer(this, 0, 0);
		Front.addBox(-3.5F, -2F, -4.5F, 7, 4, 1);
		Front.setRotationPoint(0F, 18F, 0F);
		Front.setTextureSize(64, 32);
		Front.mirror = true;
		setRotation(Front, 0F, 0F, 0F);
		Right = new ModelRenderer(this, 0, 5);
		Right.addBox(-3.5F, -2F, -4.5F, 7, 4, 1);
		Right.setRotationPoint(0F, 18F, 0F);
		Right.setTextureSize(64, 32);
		Right.mirror = true;
		setRotation(Right, 0F, 1.570796F, 0F);
		Back = new ModelRenderer(this, 0, 5);
		Back.addBox(-3.5F, -2F, -4.5F, 7, 4, 1);
		Back.setRotationPoint(0F, 18F, 0F);
		Back.setTextureSize(64, 32);
		Back.mirror = true;
		setRotation(Back, 0F, 3.141593F, 0F);
		Left = new ModelRenderer(this, 0, 5);
		Left.addBox(-3.5F, -2F, -4.5F, 7, 4, 1);
		Left.setRotationPoint(0F, 18F, 0F);
		Left.setTextureSize(64, 32);
		Left.mirror = true;
		setRotation(Left, 0F, -1.570796F, 0F);
		Corner1 = new ModelRenderer(this, 0, 10);
		Corner1.addBox(3.5F, 1F, 3.5F, 1, 1, 1);
		Corner1.setRotationPoint(0F, 18F, 0F);
		Corner1.setTextureSize(64, 32);
		Corner1.mirror = true;
		setRotation(Corner1, 0F, 0F, 0F);
		Corner2 = new ModelRenderer(this, 0, 10);
		Corner2.addBox(3.5F, 1F, 3.5F, 1, 1, 1);
		Corner2.setRotationPoint(0F, 18F, 0F);
		Corner2.setTextureSize(64, 32);
		Corner2.mirror = true;
		setRotation(Corner2, 0F, 3.141593F, 0F);
		Corner3 = new ModelRenderer(this, 0, 10);
		Corner3.addBox(3.5F, 1F, 3.5F, 1, 1, 1);
		Corner3.setRotationPoint(0F, 18F, 0F);
		Corner3.setTextureSize(64, 32);
		Corner3.mirror = true;
		setRotation(Corner3, 0F, -1.570796F, 0F);
		Corner4 = new ModelRenderer(this, 0, 10);
		Corner4.addBox(3.5F, 1F, 3.5F, 1, 1, 1);
		Corner4.setRotationPoint(0F, 18F, 0F);
		Corner4.setTextureSize(64, 32);
		Corner4.mirror = true;
		setRotation(Corner4, 0F, 1.570796F, 0F);
		Lip1 = new ModelRenderer(this, 16, 8);
		Lip1.addBox(-3.5F, 1F, -5.5F, 7, 1, 1);
		Lip1.setRotationPoint(0F, 18F, 0F);
		Lip1.setTextureSize(64, 32);
		Lip1.mirror = true;
		setRotation(Lip1, 0F, 0F, 0F);
		Lip3 = new ModelRenderer(this, 16, 8);
		Lip3.addBox(-3.5F, 1F, -5.5F, 7, 1, 1);
		Lip3.setRotationPoint(0F, 18F, 0F);
		Lip3.setTextureSize(64, 32);
		Lip3.mirror = true;
		setRotation(Lip3, 0F, 3.141593F, 0F);
		Lip4 = new ModelRenderer(this, 16, 8);
		Lip4.addBox(-3.5F, 1F, -5.5F, 7, 1, 1);
		Lip4.setRotationPoint(0F, 18F, 0F);
		Lip4.setTextureSize(64, 32);
		Lip4.mirror = true;
		setRotation(Lip4, 0F, -1.570796F, 0F);
		Lip2 = new ModelRenderer(this, 16, 8);
		Lip2.addBox(-3.5F, 1F, -5.5F, 7, 1, 1);
		Lip2.setRotationPoint(0F, 18F, 0F);
		Lip2.setTextureSize(64, 32);
		Lip2.mirror = true;
		setRotation(Lip2, 0F, 1.570796F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		if(this.isChild)
		{
			float var8 = 2.0F;
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
			boolean sit = ((EntityMet)entity).isSitting(), hide = ((EntityMet)entity).isHiding();
			if(sit||hide)
	    	{
	    		GL11.glTranslatef(0,3.0F*f5, 0);
	    	}
			Top.render(f5);
			Front.render(f5);
			Right.render(f5);
			Back.render(f5);
			Left.render(f5);
			Corner1.render(f5);
			Corner2.render(f5);
			Corner3.render(f5);
			Corner4.render(f5);
			Lip1.render(f5);
			Lip3.render(f5);
			Lip4.render(f5);
			Lip2.render(f5);
			GL11.glPopMatrix();
		}
		else
		{
			GL11.glPushMatrix();
			boolean sit = ((EntityMet)entity).isSitting(), hide = ((EntityMet)entity).isHiding();
			if(sit||hide)
	    	{
	    		GL11.glTranslatef(0,3.0F*f5, 0);
	    	}
			Top.render(f5);
			Front.render(f5);
			Right.render(f5);
			Back.render(f5);
			Left.render(f5);
			Corner1.render(f5);
			Corner2.render(f5);
			Corner3.render(f5);
			Corner4.render(f5);
			Lip1.render(f5);
			Lip3.render(f5);
			Lip4.render(f5);
			Lip2.render(f5);
			GL11.glPopMatrix();
		}
	}

	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity par7Entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, par7Entity);
	}

}
