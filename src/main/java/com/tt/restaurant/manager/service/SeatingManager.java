package com.tt.restaurant.manager.service;

import com.tt.restaurant.manager.model.CustomerGroup;
import com.tt.restaurant.manager.model.Table;

public interface SeatingManager {

    void arrives(CustomerGroup group);

    void leaves(CustomerGroup group);

    Table locate(CustomerGroup group);
}
