package org.jboss.aerogear.keycloak.metrics;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class MetricsEventListenerFactory implements EventListenerProviderFactory {

    private MetricsEventListener metricsEventListener;

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        metricsEventListener = new MetricsEventListener();
        return metricsEventListener;
    }

    @Override
    public void init(Config.Scope config) {
        // nothing to do
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        KeycloakSession session = factory.create();
        session.getTransactionManager().begin();

        session.realms().getRealmsStream().forEach(model -> metricsEventListener.initRealm(model.getId()));

        // TODO: Init the clients and providers

        session.getTransactionManager().commit();
    }

    @Override
    public void close() {
        // nothing to do
    }

    @Override
    public String getId() {
        return MetricsEventListener.ID;
    }
}
