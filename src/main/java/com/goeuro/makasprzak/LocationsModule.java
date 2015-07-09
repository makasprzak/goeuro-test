package com.goeuro.makasprzak;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.name.Names;

public class LocationsModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(String.class)
                .annotatedWith(Names.named("endpointUrl"))
                .toInstance("http://api.goeuro.com/api/v2/position/suggest/en/");
        bind(HttpTransport.class).to(ApacheHttpTransport.class);
    }

    public static LocationsCmdClient create() {
        return Guice.createInjector(new LocationsModule()).getInstance(LocationsCmdClient.class);
    }

    public static void main(String[] args) {
        System.out.println(create().execute(args));
    }
}
