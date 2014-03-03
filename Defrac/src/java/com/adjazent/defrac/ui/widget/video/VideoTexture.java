package com.adjazent.defrac.ui.widget.video;

import com.adjazent.defrac.core.stage.StageProvider;
import defrac.annotation.MacroWeb;
import defrac.display.*;
import defrac.event.EnterFrameEvent;
import defrac.event.Events;
import defrac.gl.GL;
import defrac.gl.GLTexture;
import defrac.lang.Procedure;

/**
 * @author Alan Ross
 * @version 0.1
 */
public final class VideoTexture
{
	@MacroWeb( "com.adjazent.defrac.ui.video.NativeVideo.uploadVideoTexture" )
	private static native boolean uploadVideoTexture( GL gl );

	private final TextureData _textureData;
	private final Stage stage;
	private final int width;
	private final int height;

	public VideoTexture( int width, int height )
	{
		this.width = width;
		this.height = height;

		this.stage = StageProvider.stage;

		byte[] pixels = new byte[ width * height * 4 ];

		for( int i = 0; i < pixels.length; i += 4 )
		{
			pixels[ i ] = 0;
			pixels[ i + 1 ] = 0;
			pixels[ i + 2 ] = 0;
			pixels[ i + 3 ] = 127;
		}

		_textureData = TextureData.Persistent.fromData(
				pixels,
				width,
				height,
				TextureDataFormat.RGBA,
				TextureDataRepeat.REPEAT,
				TextureDataSmoothing.NO_SMOOTHING
		);

		Events.onEnterFrame.attach( new Procedure<EnterFrameEvent>()
		{
			@Override
			public void apply( EnterFrameEvent event )
			{
				onEnterFrame();
			}
		} );
	}

	private void onEnterFrame()
	{
		GL gl = stage.gl();

		GLTexture texture = stage.getOrCreateTexture( _textureData );

		gl.bindTexture( gl.TEXTURE_2D, texture );

		uploadVideoTexture( gl );

		gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.NEAREST );
		gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.NEAREST );
		gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_WRAP_S, gl.CLAMP_TO_EDGE );
		gl.texParameteri( gl.TEXTURE_2D, gl.TEXTURE_WRAP_T, gl.CLAMP_TO_EDGE );
	}

	public Texture createTexture()
	{
		return new Texture( _textureData, 0, 0, width, height );
	}

	public Texture createTexture( float offsetX, float offsetY, float trimmedWidth, float trimmedHeight )
	{
		return new Texture( _textureData, 0, 0, width, height, 0, offsetX, offsetY, trimmedWidth, trimmedHeight );
	}

	@Override
	public String toString()
	{
		return "[VideoTexture]";
	}
}