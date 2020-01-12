// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.authservice.infra;

import io.vlingo.actors.World;
import io.vlingo.http.resource.Server;

public class Bootstrap
{
    private static Bootstrap instance;
    public final Server server;
    public final World world;
    
    private Bootstrap()
    {
        this.world = World.startWithDefaults( "authservice" );
        this.server = Server.startWith( world.stage() );
        
        Runtime.getRuntime().addShutdownHook( new Thread() 
        {
            /* @see java.lang.Thread#run() */
            @Override
            public void run()
            {
                if ( instance != null )
                {
                    instance.server.stop();
                    
                    System.out.println( "=====================" );
                    System.out.println( "Stopping authservice." );
                    System.out.println( "=====================" );
                    pause();
                }
            }
        });
    }
    
    private void pause()
    {
        try
        {
            Thread.sleep( 1000L );
        }
        catch ( Exception e )
        {
            // ignore
        }
    }
    
    public static final Bootstrap instance()
    {
        if ( instance == null ) instance = new Bootstrap();
        return instance;
    }
    
    public static void main( String[] args )
    throws Exception
    {
        System.out.println( "====================" );
        System.out.println( "authservice: started" );
        System.out.println( "====================" );
        Bootstrap.instance();
    }
}
