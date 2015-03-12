package net.aegistudio.transparent.opengl.lighting;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.aegistudio.transparent.util.Bindable;
import net.aegistudio.transparent.util.BindingFailureException;
import net.aegistudio.transparent.util.Scoped;

public class Light implements Scoped, Bindable
{
	private static boolean[] allocated = null;
	protected boolean isColorDirty = true;
	
	@Override
	public void bind()
	{
		if(!isCreated) throw new BindingFailureException("You should create the light before binding it!");
		GL11.glEnable(lightParameter);
		GL11.glLight(lightParameter, GL11.GL_POSITION, this.position_buffer);
		GL11.glLight(lightParameter, GL11.GL_SPOT_DIRECTION, this.spotlight_buffer);
		if(this.isColorDirty)
		{
			GL11.glLight(lightParameter, GL11.GL_AMBIENT, this.ambientBuffer);
			GL11.glLight(lightParameter, GL11.GL_DIFFUSE, this.diffuseBuffer);
			GL11.glLight(lightParameter, GL11.GL_SPECULAR, this.specularBuffer);
			isColorDirty = false;
		}
		if(this.isSpotlightDirty)
		{
			GL11.glLightf(lightParameter, GL11.GL_SPOT_CUTOFF, this.spotlight_cutoff);
			GL11.glLightf(lightParameter, GL11.GL_SPOT_EXPONENT, this.spotlight_exponent);
			GL11.glLightf(lightParameter, GL11.GL_QUADRATIC_ATTENUATION, this.quadratic);
			GL11.glLightf(lightParameter, GL11.GL_LINEAR_ATTENUATION, this.linear);
			GL11.glLightf(lightParameter, GL11.GL_CONSTANT_ATTENUATION, this.constant);
			isSpotlightDirty = false;
		}
	}
	
	@Override
	public void unbind()
	{
		if(!isCreated) throw new BindingFailureException("You should create the light before unbinding it!");
		GL11.glDisable(lightParameter);
	}

	protected int lightIndex;
	protected int lightParameter;
	protected boolean isCreated = false;
	
	public static void allocateLights()
	{
		if(allocated == null)
		{
			int maxLights = GL11.glGetInteger(GL11.GL_MAX_LIGHTS);
			allocated = new boolean[maxLights];
			for(int i = 0; i < maxLights; i ++) allocated[i] = false;
		}
	}
	
	@Override
	public int create()
	{
		if(!isCreated)
		{
			allocateLights();
			for(int i = 0; i < allocated.length; i ++) if(!allocated[i])
			{
				allocated[i] = true;
				lightIndex = i;
				lightParameter = GL11.GL_LIGHT0 + lightIndex;
				isCreated = true;
				break;
			}
			if(!isCreated) throw new BindingFailureException("Unable to create more space for this light!");
		}
		return lightIndex;
	}

	protected FloatBuffer position_buffer;
	{
		this.position_buffer = BufferUtils.createFloatBuffer(4).put(new float[]{0.F, 0.F, 0.F, 1.F});
		this.position_buffer.flip();
	}
	
	/**
	 * Sets the position of the light, whether the w is 0.F determines that the light is positional
	 * or directional.
	 * @param x X-axis of the light.
	 * @param y Y-axis of the light.
	 * @param z Z-axis of the light.
	 * @param w w parameter of the light.
	 */
	
	public void position(float x, float y, float z, float w)
	{
		this.position_buffer = BufferUtils.createFloatBuffer(4).put(new float[]{x, y, z, w});
		this.position_buffer.flip();
	}
	
	protected FloatBuffer ambientBuffer;
	{
		ambientBuffer = BufferUtils.createFloatBuffer(4).put(new float[]{0.F, 0.F, 0.F, 1.F});
		ambientBuffer.flip();
	}
	
	/**
	 * Set the ambient color of the light.
	 * @param ambient_r the red of the ambient color
	 * @param ambient_g the green of the ambient color
	 * @param ambient_b the blue of the ambient color
	 * @param ambient_alpha the alpha of the ambient color
	 */
	
	public void ambient(float ambient_r, float ambient_g, float ambient_b, float ambient_alpha)
	{
		ambientBuffer = BufferUtils.createFloatBuffer(4).put(new float[]{ambient_r, ambient_g, ambient_b, ambient_alpha});
		ambientBuffer.flip();
		isColorDirty = true;
	}
	
	protected FloatBuffer diffuseBuffer;
	{
		diffuseBuffer = BufferUtils.createFloatBuffer(4).put(new float[]{0.F, 0.F, 0.F, 1.F});
		diffuseBuffer.flip();
	}
	
	/**
	 * Set the diffuse color of the light.
	 * @param diffuse_r the red of the diffuse color
	 * @param diffuse_g the green of the diffuse color
	 * @param diffuse_b the blue of the diffuse color
	 * @param diffuse_alpha the alpha of the diffuse color
	 */
	
	public void diffuse(float diffuse_r, float diffuse_g, float diffuse_b, float diffuse_alpha)
	{
		diffuseBuffer = BufferUtils.createFloatBuffer(4).put(new float[]{diffuse_r, diffuse_g, diffuse_b, diffuse_alpha});
		diffuseBuffer.flip();
		isColorDirty = true;
	}
	
	protected FloatBuffer specularBuffer;
	{
		specularBuffer = BufferUtils.createFloatBuffer(4).put(new float[]{0.F, 0.F, 0.F, 1.F});
		specularBuffer.flip();
	}
	
	/**
	 * Set the specular color of the light.
	 * @param specular_r the red of the specular color
	 * @param specular_g the green of the specular color
	 * @param specular_b the blue of the specular color
	 * @param specular_alpha the alpha of the specular color
	 */
	
	public void specular(float specular_r, float specular_g, float specular_b, float specular_alpha)
	{
		specularBuffer = BufferUtils.createFloatBuffer(4).put(new float[]{specular_r, specular_g, specular_b, specular_alpha});
		specularBuffer.flip();
		isColorDirty = true;
	}
	
	protected FloatBuffer spotlight_buffer;
	{
		this.spotlight_buffer = BufferUtils.createFloatBuffer(4).put(new float[]{0.F, 0.F, -1.F, 1.F});
		this.spotlight_buffer.flip();
	}
	
	/**
	 * Set the spotlight orientation of the light.
	 * @param spotlight_x
	 * @param spotlight_y
	 * @param spotlight_z
	 * @param spotlight_w
	 */
	
	public void orient(float spotlight_x, float spotlight_y, float spotlight_z)
	{
		this.spotlight_buffer = BufferUtils.createFloatBuffer(4).put(new float[]{spotlight_x, spotlight_y, spotlight_z, 1.F});
		this.spotlight_buffer.flip();
	}
	
	protected boolean isSpotlightDirty = true;
	
	protected float spotlight_cutoff = 180.F, spotlight_exponent = 0.F;
	
	/**
	 * Set the other parameters of the spotlight.
	 * @param spotlight_cutoff the cut-off angle of the spotlight.
	 * @param spotlight_exponent the exponent of the spotlight.
	 */
	public void spotlight(float spotlight_cutoff, float spotlight_exponent)
	{
		this.spotlight_cutoff = spotlight_cutoff; this.spotlight_exponent = spotlight_exponent;
		this.isSpotlightDirty = true;
	}
	
	protected float constant = 1.F, linear = 0.F, quadratic = 0.F;
	
	/**
	 * The light attenuation equation is L(Attenuation) = L(Origin) / (C + L*R + Q * R^2)
	 * Here R is the distant between the fragment and light source, L is the light amplitude.
	 * @param constant The C factor in the equation.
	 * @param linear The L factor in the equation.
	 * @param quadratic The Q factor in the equation.
	 */
	
	public void attenuation(float constant, float linear, float quadratic)
	{
		this.constant = constant; this.linear = linear; this.quadratic = quadratic;
		this.isSpotlightDirty = true;
	}
	
	@Override
	public void destroy()
	{
		if(isCreated)
		{
			allocated[lightIndex] = false;
			isCreated = false;
		}
	}
	
	/**
	 * Used for debugging.
	 */
	
	/*
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder("Light[");
		stringBuilder.append("gl_lighting_index=");
		if(isCreated) stringBuilder.append(Integer.toString(lightIndex));
		else stringBuilder.append("not-available");
		stringBuilder.append(", ambient="); stringBuilder.append("(" + this.ambient_r + ", " + this.ambient_g + ", " + this.ambient_b + ", " + this.ambient_alpha + ")");
		stringBuilder.append(", diffuse="); stringBuilder.append("(" + this.diffuse_r + ", " + this.diffuse_g + ", " + this.diffuse_b + ", " + this.diffuse_alpha + ")");
		stringBuilder.append(", specular="); stringBuilder.append("(" + this.specular_r + ", " + this.specular_g + ", " + this.specular_b + ", " + this.specular_alpha + ")");
		stringBuilder.append(", position="); stringBuilder.append("(" + this.position_buffer.get(0) + ", " + this.position_buffer.get(1) + ", " + this.position_buffer.get(2) + ", " + this.position_buffer.get(3) + ")");
		stringBuilder.append(", orientation="); stringBuilder.append("(" + this.spotlight_buffer.get(0) + ", " + this.spotlight_buffer.get(1) + ", " + this.spotlight_buffer.get(2) + ")");
		stringBuilder.append(", spotlight_cutoff="); stringBuilder.append(Float.toString(this.spotlight_cutoff));
		stringBuilder.append(", spotlight_exponent="); stringBuilder.append(Float.toString(this.spotlight_exponent));
		stringBuilder.append(", attenuation="); stringBuilder.append("(" + this.constant + ", " + this.linear + ", " + this.quadratic + ")");
		
		stringBuilder.append("]@"); stringBuilder.append(Integer.toHexString(this.hashCode()));
		return new String(stringBuilder);
	}
	*/
}
