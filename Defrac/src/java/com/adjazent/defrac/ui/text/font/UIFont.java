package com.adjazent.defrac.ui.text.font;

import com.adjazent.defrac.ds.atlas.Atlas;
import com.adjazent.defrac.ds.atlas.IAtlasElement;
import com.adjazent.defrac.ui.text.font.glyph.UIGlyph;
import defrac.display.TextureData;

/**
 * @author Alan Ross
 * @version 0.1
 */
public final class UIFont extends Atlas implements IAtlasElement
{
	private final TextureData _textureData;
	private final String _id;

	private String _face;
	private int _size;
	private int _lineHeight;
	private boolean _bold;
	private boolean _italic;

	public UIFont( TextureData textureData, String id, String face, int size, boolean bold, boolean italic, int lineHeight )
	{
		_textureData = textureData;
		_id = id;
		_face = face;
		_size = size;
		_bold = bold;
		_italic = italic;
		_lineHeight = lineHeight;
	}

	public Boolean addGlyph( UIGlyph glyph )
	{
		return addElement( glyph );
	}

	public Boolean removeGlyph( UIGlyph glyph )
	{
		return removeElement( glyph );
	}

	public UIGlyph getGlyph( char character )
	{
		int code = UIGlyph.charToCode( character );

		return getGlyph( code );
	}

	public UIGlyph getGlyph( int code )
	{
		return ( ( UIGlyph ) getElement( Integer.toString( code ) ) ).clone();
	}

	public boolean hasGlyph( char character )
	{
		int code = UIGlyph.charToCode( character );

		return hasGlyph( code );
	}

	public boolean hasGlyph( int code )
	{
		return hasElement( Integer.toString( code ) );
	}

	@Override
	public void dispose()
	{
		super.dispose();

		_face = null;
	}

	public TextureData getTextureData()
	{
		return _textureData;
	}

	public String getFace()
	{
		return _face;
	}

	public int getSize()
	{
		return _size;
	}

	public boolean getBold()
	{
		return _bold;
	}

	public boolean getItalic()
	{
		return _italic;
	}

	public int getLineHeight()
	{
		return _lineHeight;
	}

	@Override
	public String getId()
	{
		return _id;
	}

	@Override
	public String toString()
	{
		return "[UIFont" +
				" id:" + getId() +
				", fontId:" + getFace() +
				", size:" + getSize() +
				", bold:" + getBold() +
				", italic:" + getItalic() + "]";
	}
}

