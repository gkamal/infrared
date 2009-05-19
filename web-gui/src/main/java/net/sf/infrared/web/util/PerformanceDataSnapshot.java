/*
 *
 * Copyright 2005 Tavant Technologies and Contributors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 * Original Author:  prashant.nair (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.infrared.aspects.jdbc.SqlExecuteContext;
import net.sf.infrared.aspects.jdbc.SqlPrepareContext;
import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.ExecutionContext;
import net.sf.infrared.base.model.StatisticsSnapshot;

public class PerformanceDataSnapshot {
    
    //[BINIL] private ApplicationStatistics stats;
    private StatisticsSnapshot stats;    
    private Set applicationNames;
    private Set instanceNames;
    
    public Set getApplicationNames() {
        return applicationNames;
    }
    public void setApplicationNames(Set applicationNames) {
        this.applicationNames = applicationNames;
    }
    public Set getInstanceNames() {
        return instanceNames;
    }
    public void setInstanceNames(Set instanceNames) {
        this.instanceNames = instanceNames;
    }
    //[BINIL] public ApplicationStatistics getStats() {
    public StatisticsSnapshot getStats() {
        return stats;
    }
    
    public void setStats(StatisticsSnapshot stats) {
        this.stats = stats;
    }
    
    public SqlStatistics[] getSqlStatistics(){
        Map sqlStatsMap = new HashMap();
        SqlStatistics sqlStats;
        String[] layers = stats.getHierarchicalLayers();
        for (int i = 0; i < layers.length; i++) {
            String layer = layers[i];
            if (! layer.endsWith("SQL")) {
                continue;    
            }
            AggregateExecutionTime[] sqlTimes = stats.getExecutionsInHierarchicalLayer(layer);
            for (int j = 0; j < sqlTimes.length; j++) {
                AggregateExecutionTime aet = (AggregateExecutionTime) sqlTimes[j];
                ExecutionContext ctx = aet.getContext();
            
                if (ctx instanceof SqlPrepareContext) {
                    String sql = ctx.getName();
                    sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                    sqlStats.mergePrepareTime(aet);
                } else if(ctx instanceof SqlExecuteContext) {
                    String sql = ctx.getName();
                    sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                    sqlStats.mergeExecuteTime(aet);
                }
            }
        }
        return (SqlStatistics[]) sqlStatsMap.values().toArray(new SqlStatistics[0]);
    }
    
    public SqlStatistics[] getSqlStatisticsForLayer(String layer, String layerType){

        Map sqlStatsMap = new HashMap();
        SqlStatistics sqlStats;
        AggregateExecutionTime[] sqlTimes = null;
        if(layerType.equals("hier")){
            sqlTimes = stats.getExecutionsInHierarchicalLayer(layer); 
        }
        else if(layerType.equals("abs")){
            sqlTimes = stats.getExecutionsInAbsoluteLayer(layer);
        }

        for (int j = 0; j < sqlTimes.length; j++) {
            AggregateExecutionTime aet = (AggregateExecutionTime) sqlTimes[j];
            ExecutionContext ctx = aet.getContext();
        
            if (ctx instanceof SqlPrepareContext) {
                String sql = ctx.getName();
                sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                sqlStats.mergePrepareTime(aet);
            } 
            else if(ctx instanceof SqlExecuteContext) {
                String sql = ctx.getName();
                sqlStats = getSqlStatsFromMap(sqlStatsMap, sql);
                sqlStats.mergeExecuteTime(aet);
            }
        }
        return (SqlStatistics[]) sqlStatsMap.values().toArray(new SqlStatistics[0]);
    }

    public SqlStatistics getSqlStatsFromMap(Map sqlStatsMap, String sql){
        SqlStatistics stats;
        if(sqlStatsMap.get(sql) == null){
            stats = new SqlStatistics();
            sqlStatsMap.put(sql, stats);
        }
        else{
            stats = (SqlStatistics)sqlStatsMap.get(sql);
        }        
        return stats;        
    }    
}
