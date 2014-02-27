package com.adjazent.defrac.ui.surface;

import com.adjazent.defrac.core.error.UnsupportedOperationError;
import com.adjazent.defrac.core.utils.IDisposable;
import com.adjazent.defrac.math.geom.MRectangle;
import com.adjazent.defrac.ui.surface.skin.UISurfaceSkinFactory;
import com.adjazent.defrac.ui.texture.UITexture;
import defrac.display.DisplayObject;
import defrac.display.DisplayObjectContainer;
import defrac.display.event.UIEventTarget;
import defrac.geom.Point;

/**
 * @author Alan Ross
 * @version 0.1
 */
public class UISurface extends DisplayObjectContainer implements IDisposable
{
	public String id = "";

	private MRectangle _bounds;

	private IUISkin _skin;

	public UISurface( UITexture texture )
	{
		super();

		_bounds = new MRectangle( 0, 0, ( float ) texture.getSkinRect().width, ( float ) texture.getSkinRect().height );

		setTexture( texture );
	}

	public UISurface( int color )
	{
		super();

		_bounds = new MRectangle( 0, 0, 1, 1 );

		setColor( color );
	}

	private void detachSkin( IUISkin skin )
	{
		if( skin != null )
		{
			skin.detach( this );

			UISurfaceSkinFactory.release( skin );
		}
	}

	private void attachSkin( IUISkin skin )
	{
		_skin = skin;
		_skin.attach( this );
		_skin.resizeTo( ( float ) _bounds.width, ( float ) _bounds.height );
	}

	@Override
	public UIEventTarget captureEventTarget( @javax.annotation.Nonnull Point point )
	{
		Point local = this.globalToLocal( new Point( point.x, point.y ) );

		return ( containsPoint( local.x, local.y ) ) ? this : null;
	}

	public void setTexture( UITexture texture )
	{
		detachSkin( _skin );

		_skin = null;

		attachSkin( UISurfaceSkinFactory.create( texture ) );
	}

	public void setColor( int color )
	{
		detachSkin( _skin );

		_skin = null;

		attachSkin( UISurfaceSkinFactory.create( color ) );
	}

	public void moveTo( int x, int y )
	{
		super.moveTo( x, y );
	}

	public DisplayObject resizeTo( float width, float height )
	{
		_bounds.resizeTo( width, height );
		_skin.resizeTo( width, height );

		return this;
	}

	@Override
	public DisplayObject scaleToSize( float width, float height )
	{
		throw new UnsupportedOperationError( this );
	}

	@Override
	public DisplayObject scaleTo( float sx, float sy )
	{
		throw new UnsupportedOperationError( this );
	}

	@Override
	public DisplayObject scaleBy( float dx, float dy )
	{
		throw new UnsupportedOperationError( this );
	}

	public boolean containsPoint( float x, float y )
	{
		return _bounds.contains( x, y );
	}

	public DisplayObjectContainer getRoot()
	{
		DisplayObjectContainer o = this;

		while( o.parent() != null )
		{
			o = o.parent();
		}

		return o;
	}

	@Override
	public void dispose()
	{
		detachSkin( _skin );

		_skin = null;
	}

	@Override
	public String toString()
	{
		return "[UISurface id:" + id + "]";
	}
}

