package framework.provider;

import framework.service_interface.Connection;

public interface Driver {
    Connection connect(String url);

    String getName();

    boolean acceptsURL(String url);
}
