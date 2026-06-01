package org.auspicode.cml.realestatedbaccess.services;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.mssqlserver.MSSQLServerContainer;

public abstract class DbTestContainer {

    @ServiceConnection
    static final MSSQLServerContainer mssql =
            new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2022-latest")
                    .acceptLicense();
}