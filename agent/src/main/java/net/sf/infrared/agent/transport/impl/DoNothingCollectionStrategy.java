package net.sf.infrared.agent.transport.impl;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.base.model.OperationStatistics;

public class DoNothingCollectionStrategy implements CollectionStrategy {

    public boolean init(MonitorConfig configuration) {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean collect(OperationStatistics stats) {
        // TODO Auto-generated method stub
        return true;
    }

    public void suspend() {
        // TODO Auto-generated method stub

    }

    public void resume() {
        // TODO Auto-generated method stub

    }

    public boolean destroy() {
        // TODO Auto-generated method stub
        return true;
    }

}
