package com.adjazent.defrac.sandbox;

import com.adjazent.defrac.core.error.GenericError;
import com.adjazent.defrac.core.log.Context;
import com.adjazent.defrac.core.log.Level;
import com.adjazent.defrac.core.log.Log;
import com.adjazent.defrac.core.log.output.SilentLogOutput;
import com.adjazent.defrac.core.log.output.SimpleLogOutput;
import com.adjazent.defrac.sandbox.experiments.Experiment;
import defrac.app.GenericApp;
import defrac.display.Layer;
import defrac.display.Stats;
import defrac.event.StageEvent;
import defrac.lang.Procedure;

import java.util.Hashtable;

/**
 * @author Alan Ross
 * @version 0.1
 */
public class AbstractSandbox extends GenericApp
{
	private final Procedure<StageEvent.Resize> resizeProcedure = new Procedure<StageEvent.Resize>()
	{
		@Override
		public void apply( StageEvent.Resize event )
		{
			onAppResize();
		}
	};

	protected Layer container;
	protected Stats stats;

	private Hashtable<String, Experiment> _experiments;
	private Experiment _experiment;

	@Override
	protected final void onCreate()
	{
		super.onCreate();

		Log.initialize();
		Log.get().addOutput( new SimpleLogOutput() );
		Log.get().addOutput( new SilentLogOutput() );

		Context.setLevels( Context.DEFAULT, Level.TRACE, Level.FATAL );
		Context.setLevels( Context.UI, Level.TRACE, Level.FATAL );
		Context.setLevels( Context.NET, Level.TRACE, Level.FATAL );
		Context.setLevels( Context.TIME, Level.TRACE, Level.FATAL );

		container = addChild( new Layer() );
		stats = addChild( new Stats() );

//		container.centerRegistrationPoint().moveBy( 100, 100 );

		stage().onResize.attach( resizeProcedure );

		onAppResize();

		onCreateComplete();

	}

	protected void onCreateComplete()
	{

	}

	protected void onAppResize()
	{
		stats.moveTo( 10, 10 );
//		container.moveTo( ( stage().width() - container.width() ) * 0.5f, ( stage().height() - container.height() ) * 0.5f );

		if( _experiment != null )
		{
			_experiment.resizeTo( stage().width(), stage().height() );
		}
	}

	protected void add( Experiment experiment )
	{
		if( _experiments == null )
		{
			_experiments = new Hashtable<String, Experiment>();
		}

		String key = experiment.getClass().getName();

		_experiments.put( key, experiment );
	}

	protected void activate( Object clazz )
	{
		if( _experiment != null )
		{
			container.removeChild( _experiment );

			_experiment = null;
		}

		String key = clazz.toString();

		if( key.indexOf( ' ' ) > -1 )
		{
			//clazz.toString() resturns "class com.package...."
			key = key.substring( key.indexOf( ' ' ) + 1 );
		}

		_experiment = _experiments.get( key );

		if( _experiment == null )
		{
			throw new GenericError( this, " No example found with id:" + clazz );
		}

		Log.info( Context.DEFAULT, this, "Running: " + _experiment );

		container.addChild( _experiment );
		_experiment.init( stage(), this );
	}

	@Override
	public String toString()
	{
		return "[AbstractSandbox]";
	}
}
