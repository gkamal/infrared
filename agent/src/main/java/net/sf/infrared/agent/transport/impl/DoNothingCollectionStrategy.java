package net.sf.infrared.agent.transport.impl;

import net.sf.infrared.agent.MonitorConfig;
import net.sf.infrared.agent.transport.CollectionStrategy;
import net.sf.infrared.base.model.OperationStatistics;

public class DoNothingCollectionStrategy implements CollectionStrategy {

    public boolean init(MonitorConfig configuration) {
        // ignore
        return true;
    }

    public boolean collect(OperationStatistics stats) {
        // ignore
        return true;
    }

    public void suspend() {
        // ignore
    }

    public void resume() {
        // ignore
    }

    public boolean destroy() {
        // ignore
        return true;
    }

}
