/*
 * Copyright (C) 2015 Nipuna Gunathilake.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usf.cutr.gtfsrtvalidator;

import edu.usf.cutr.gtfsrtvalidator.db.Datasource;
import edu.usf.cutr.gtfsrtvalidator.db.GTFSDB;
import edu.usf.cutr.gtfsrtvalidator.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    static String BASE_RESOURCE = "./target/classes/webroot";

    public Main() throws SQLException, IOException, PropertyVetoException {

    }

    public static void main(String[] args) throws Exception{
        new Main();

        GTFSDB.InitializeDB();

        Datasource ds = Datasource.getInstance();
        ds.getConnection();

        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler();

        context.setContextPath("/");
        context.setResourceBase(BASE_RESOURCE);

        server.setHandler(context);

        context.addServlet(RTFeedValidatorServlet.class, "/validate");
        context.addServlet(GTFSDownloaderServlet.class, "/downloadgtfs");

        context.addServlet(CountServlet.class, "/count");
        context.addServlet(FeedInfoServlet.class, "/feedInfo");
        context.addServlet(TriggerBackgroundServlet.class, "/startBackground");


        context.addServlet(DefaultServlet.class, "/");

        server.start();
        server.join();
    }
}
