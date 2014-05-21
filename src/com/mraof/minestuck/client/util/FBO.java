package com.mraof.minestuck.client.util;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL14;

public class FBO {
	public int width, height;
	public int id, texId, depthId;
	public boolean disposed;

	public FBO(int w, int h) {
		width = w;
		height = h;

		init();
	}

	private void init() {
		disposed = false;
		id = glGenFramebuffersEXT();
		texId = glGenTextures();
		depthId = glGenRenderbuffersEXT();
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
		
		glBindTexture(GL_TEXTURE_2D, texId);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA,
				GL_INT, (java.nio.ByteBuffer) null);
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT,
				GL_TEXTURE_2D, texId, 0);
		
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthId);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT,
				GL14.GL_DEPTH_COMPONENT24, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,
				GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthId);
		
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		
	}

	public void bind() {
		if(disposed)
			throw new IllegalStateException("Can't bind while the Framebuffer is disposed!");
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, id);
		
		glViewport(0, 0, width, height);
		
		glClearColor(0, 0, 0, 0);
		glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
	}

	public void unbind() {
		if(disposed)
			throw new IllegalStateException("Can't unbind while the Framebuffer is disposed!");
		
		glBindTexture(GL_TEXTURE_2D, texId);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public void dispose() {
		if(!disposed) {
			glDeleteFramebuffersEXT(id);
			glDeleteRenderbuffersEXT(depthId);
			glDeleteTextures(texId);
			disposed = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		dispose();
	}
	
}