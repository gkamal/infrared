/* 
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
 * Original Author:  kamal.govindraj (Tavant Technologies)
 * Contributor(s):   -;
 *
 */
package net.sf.infrared.web.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.infrared.base.model.AggregateExecutionTime;
import net.sf.infrared.base.model.LayerTime;
import net.sf.infrared.web.util.PerformanceDataSnapshot;
import net.sf.infrared.web.util.SqlStatistics;
import net.sf.infrared.web.util.ViewUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * This class creates a summary report as excel workbook
 * The excel file is created using the Jakarta POI api.
 */
public class ExcelSummaryReport implements SummaryReport {
	private HSSFWorkbook wb = null;

	private HSSFCellStyle defaultStyleForDouble = null;

	private HSSFCellStyle mainHeadingStyle = null;

	private HSSFCellStyle subHeadingStyle = null;

	private HSSFCellStyle columnHeadingStyle = null;

	/**
	 * Constructor
	 */
	public ExcelSummaryReport() {
		wb = new HSSFWorkbook();
		setupStyles();
	}

	/**
	 * @see SummaryReport#addSnapShot
	 * @param snapShot
	 */
	public void addSnapShot(PerformanceDataSnapshot snapShot) {
        
        System.out.println("\n\nThe Excel Report call...");
		HSSFSheet sheet1 = wb.createSheet();
		int currentRow = 0;
		currentRow = addJDBCSummary(sheet1, currentRow, snapShot);

		currentRow = insertBlankRow(sheet1, currentRow, 3);
                
        currentRow = addAPISummaryForAbsoluteLayers(sheet1, currentRow, snapShot, true);

        currentRow = insertBlankRow(sheet1, currentRow, 3);

        currentRow = addAPISummaryForAbsoluteLayers(sheet1, currentRow, snapShot, false);
        
        currentRow = insertBlankRow(sheet1, currentRow, 3);
        
		currentRow = addAPISummary(sheet1, currentRow, snapShot, true);

		currentRow = insertBlankRow(sheet1, currentRow, 3);

		currentRow = addAPISummary(sheet1, currentRow, snapShot, false);
        
        currentRow = insertBlankRow(sheet1, currentRow, 3);
        

        

	}

	/**
	 * @see SummaryReport#save(java.lang.String)
	 * @param fileName
	 * @throws IOException
	 */
	public void save(String fileName) throws IOException {
		FileOutputStream fileOut = new FileOutputStream(fileName);
		save(fileOut);
		fileOut.close();
	}

	/**
	 * @see SummaryReport#save(java.io.OutputStream)
	 * @param os
	 * @throws IOException
	 */
	public void save(OutputStream os) throws IOException {
		wb.write(os);
	}


	private int addAPISummary(HSSFSheet sheet1, int currentRow,
			PerformanceDataSnapshot snapShot, boolean exclusive) {
        AggregateExecutionTime[] apis = null;
        
        currentRow = addMainHeading(sheet1, currentRow, "Operation Summary ( "
				+ getModeAsString(exclusive) + " ) for Hierarchical Layers");

		currentRow = insertBlankRow(sheet1, currentRow, 1);

		//[BINIL] LayerTime[] layers = snapShot.getStats().getCopyOfLayerTimes();
        // Dodgy code, layers and layerz are confusing
        String[] layerz = snapShot.getStats().getHierarchicalLayers();
        LayerTime[] layers = new LayerTime[layerz.length];        
        for (int j = 0; j < layerz.length; j++) {
            String aLayer = layerz[j];
            long itsTime = snapShot.getStats().getTimeInHierarchicalLayer(aLayer);
            LayerTime lt = new LayerTime(aLayer);
            lt.setTime(itsTime);
            layers[j] = lt;
        }        
        
        
		for (int i = 0; i < layers.length; i++) {
			LayerTime layer = layers[i];
			// Ignore JDBC layer
			if (layer.getLayer().endsWith("JDBC"))
				continue;
            
            apis = ViewUtil.getSummaryForALayer(snapShot, layer.getLayer());            
            currentRow = addSummaryForALayer(sheet1, currentRow, apis, layer.getLayer(),
                    exclusive);            
		}

		currentRow = insertBlankRow(sheet1, currentRow, 1);

		return currentRow;
	}


    private int addAPISummaryForAbsoluteLayers(HSSFSheet sheet1, int currentRow,
            PerformanceDataSnapshot snapShot, boolean exclusive) {
        AggregateExecutionTime[] apis = null;
        
        currentRow = addMainHeading(sheet1, currentRow, "Operation Summary ( "
                + getModeAsString(exclusive) + " ) for Absolute Layers");

        currentRow = insertBlankRow(sheet1, currentRow, 1);

        //[BINIL] LayerTime[] layers = snapShot.getStats().getCopyOfLayerTimes();
        // Dodgy code, layers and layerz are confusing
        String[] layerz = snapShot.getStats().getAbsoluteLayers();
        LayerTime[] layers = new LayerTime[layerz.length];        
        for (int j = 0; j < layerz.length; j++) {
            String aLayer = layerz[j];
            long itsTime = snapShot.getStats().getTimeInAbsoluteLayer(aLayer);
            LayerTime lt = new LayerTime(aLayer);
            lt.setTime(itsTime);
            layers[j] = lt;
        }        
        
        
        for (int i = 0; i < layers.length; i++) {
            LayerTime layer = layers[i];
            // Ignore JDBC layer
            if (layer.getLayer().endsWith("JDBC"))
                continue;
            
            apis = ViewUtil.getSummaryForAbsoluteLayer(snapShot, layer.getLayer());            
            currentRow = addSummaryForALayer(sheet1, currentRow, apis, layer.getLayer(),
                    exclusive);

        }
        currentRow = insertBlankRow(sheet1, currentRow, 1);

        return currentRow;
    }
    
    
    private int addSummaryForALayer(HSSFSheet sheet1, int currentRow,
            AggregateExecutionTime[] apis, String layerName, boolean exclusive) {
		addSubHeading(sheet1, currentRow++, "Summary for Layer : " + layerName);
		//[SUBIN] AggregateExecutionTime[] apis = ViewUtil.getSummaryForALayer(snapShot, layer.getLayer());
		currentRow = insertBlankRow(sheet1, currentRow, 1);
		currentRow = addTopNApiByExecutionTime(sheet1, currentRow, apis,exclusive);

		currentRow = insertBlankRow(sheet1, currentRow, 2);

		currentRow = addTopNApiByCount(sheet1, currentRow, apis, exclusive);

		currentRow = insertBlankRow(sheet1, currentRow, 2);

		currentRow = addTopNApiByTotalTime(sheet1, currentRow, apis, exclusive);

		currentRow = insertBlankRow(sheet1, currentRow, 1);
		return currentRow;
	}

	private String getModeAsString(boolean exclusiveMode) {
		if (exclusiveMode)
			return "Exclusive";
		else
			return "Inclusive";
	}

	private int addTopNApiByTotalTime(HSSFSheet sheet1, int currentRow, 
											AggregateExecutionTime[] apis, boolean exclusiveMode) {
		
		currentRow = addSubHeading(sheet1, currentRow, "Top 10 Operations by Total time");

		writeAPISummaryHeader(sheet1.createRow(currentRow++), exclusiveMode);
		ViewUtil.sort(exclusiveMode ? "totalTimeExclusive" : "totalTime", false, apis);
		currentRow = writeApiSummary(apis, sheet1, currentRow, exclusiveMode);
		return currentRow;
	}

	private int addTopNApiByCount(HSSFSheet sheet1, int currentRow,AggregateExecutionTime[] apis, 
																		boolean exclusiveMode) {
		currentRow = addSubHeading(sheet1, currentRow, "Top 10 Operations by count");
		writeAPISummaryHeader(sheet1.createRow(currentRow++), exclusiveMode);
		ViewUtil.sort("count", false, apis);
		currentRow = writeApiSummary(apis, sheet1, currentRow, exclusiveMode);
		return currentRow;
	}

	private int addTopNApiByExecutionTime(HSSFSheet sheet1, int currentRow, 
											AggregateExecutionTime[] apis, boolean exclusiveMode) {
		
		currentRow = addSubHeading(sheet1, currentRow,"Top 10 Operations by execution time");
		writeAPISummaryHeader(sheet1.createRow(currentRow++), exclusiveMode);
		ViewUtil.sort(exclusiveMode ? "adjAvgExclusive" : "adjAvg", 
																				false, apis);
		currentRow = writeApiSummary(apis, sheet1, currentRow, exclusiveMode);
		return currentRow;
	}

	private int addJDBCSummary(HSSFSheet sheet1, int currentRow, PerformanceDataSnapshot snapShot) {
		currentRow = addMainHeading(sheet1, currentRow, "JDBC Summary");
		currentRow = insertBlankRow(sheet1, currentRow, 1);
		SqlStatistics[] sqlStatistics = snapShot.getSqlStatistics();
		currentRow = addTopNSqlQueriesByExecutionTime(sheet1, currentRow,sqlStatistics, snapShot);
		currentRow = insertBlankRow(sheet1, currentRow, 1);
		currentRow = addTopNSqlQueriesByCount(sheet1, currentRow,sqlStatistics, snapShot);
		return currentRow;
	}

	private int addTopNSqlQueriesByCount(HSSFSheet sheet1, int currentRow,
								SqlStatistics[] sqlStatistics, PerformanceDataSnapshot snapShot) {
		
		currentRow = addSubHeading(sheet1, currentRow, "Top 10 queries by count");
		writeJDBCHeader(sheet1.createRow(currentRow++));
		currentRow = writeSQLStatistics(ViewUtil.getTopNQueriesByCount(sqlStatistics, 10), 
																sheet1, currentRow, snapShot);
		return currentRow;
	}

	private int addTopNSqlQueriesByExecutionTime(HSSFSheet sheet1, int currentRow, 
							SqlStatistics[] sqlStatistics, PerformanceDataSnapshot snapShot) {
		
		currentRow = addSubHeading(sheet1, currentRow, "Top 10 queries by execution time");

		ViewUtil.getTopNQueriesByCount(sqlStatistics, 10);
		writeJDBCHeader(sheet1.createRow(currentRow++));
		currentRow = writeSQLStatistics(ViewUtil.getTopNQueriesByExecutionTime(sqlStatistics, 10), 
															sheet1, currentRow, snapShot);
		return currentRow;
	}

	private int insertBlankRow(HSSFSheet sheet1, int currentRow, int noOfRows) {
		while (noOfRows-- > 0) {
			sheet1.createRow(currentRow++);
		}
		return currentRow;
	}

	private void setupStyles() {
		setUpDoubleCellStyle();
		setUpMainHeadingStyle();
		setUpSubHeadingStyle();
		setUpColumnHeadingStyle();
	}

	private void setUpColumnHeadingStyle() {
		columnHeadingStyle = wb.createCellStyle();
		HSSFFont columnHeadingFont = wb.createFont();
		columnHeadingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		columnHeadingFont.setFontHeightInPoints((short) 10);
		columnHeadingStyle.setFont(columnHeadingFont);
		columnHeadingStyle.setWrapText(true);
		columnHeadingStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		columnHeadingStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		columnHeadingStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		columnHeadingStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		columnHeadingStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		columnHeadingStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		columnHeadingStyle.setFillPattern(HSSFCellStyle.NO_FILL);
		columnHeadingStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
	}

	private void setUpSubHeadingStyle() {
		subHeadingStyle = wb.createCellStyle();
		HSSFFont subHeadingFont = wb.createFont();
		subHeadingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		subHeadingFont.setFontHeightInPoints((short) 12);
		subHeadingStyle.setFont(subHeadingFont);
	}

	private void setUpMainHeadingStyle() {
		mainHeadingStyle = wb.createCellStyle();
		HSSFFont mainHeadingFont = wb.createFont();
		mainHeadingFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		mainHeadingFont.setFontHeightInPoints((short) 14);
		mainHeadingStyle.setFont(mainHeadingFont);
	}

	private void setUpDoubleCellStyle() {
		defaultStyleForDouble = wb.createCellStyle();
		defaultStyleForDouble.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		defaultStyleForDouble.setDataFormat(wb.createDataFormat().getFormat("######0.00"));
	}

	private int addSubHeading(HSSFSheet sheet1, int currentRow, String headingText) {
		HSSFCell headingCell = sheet1.createRow(currentRow++).createCell((short) 0);
		headingCell.setCellStyle(subHeadingStyle);
		headingCell.setCellValue(headingText);
		return currentRow;
	}

	private int addMainHeading(HSSFSheet sheet1, int currentRow, String headingText) {
		HSSFCell headingCell = sheet1.createRow(currentRow++).createCell((short) 0);
		headingCell.setCellValue(headingText);
		headingCell.setCellStyle(mainHeadingStyle);
		return currentRow;
	}

	private int writeApiSummary(AggregateExecutionTime[] apis, HSSFSheet sheet1, int currentRow, 
																		boolean exclusiveMode) {
		for (int i = 0; i < apis.length && i < 10; i++) {
			writeApiSummaryData(sheet1.createRow(currentRow++), apis[i], i + 1, exclusiveMode);
		}
		return currentRow;
	}

	private void writeApiSummaryData(HSSFRow row, AggregateExecutionTime apiTime,int rowNo, 
																		boolean exclusiveMode) {
		short currentCell = 0;
		row.createCell(currentCell++).setCellValue(rowNo);
		//[BINIL]row.createCell(currentCell++).setCellValue(apiTime.getName());
        row.createCell(currentCell++).setCellValue(apiTime.getContext().getName());
		row.createCell(currentCell++).setCellValue(apiTime.getExecutionCount());
		currentCell = addDoubleCell(row, currentCell, exclusiveMode ? 
								apiTime.getTotalExclusiveTime() : apiTime.getTotalInclusiveTime());                
		currentCell = addDoubleCell(row, currentCell, exclusiveMode ? 
								apiTime.getAverageExclusiveTime() : 
								apiTime.getAverageInclusiveTime());
		currentCell = addDoubleCell(row, currentCell, exclusiveMode ? 
								apiTime.getAdjAverageExclusiveTime() : 
								apiTime.getAdjAverageInclusiveTime());        
		row.createCell(currentCell++).setCellValue(exclusiveMode ? 
									apiTime.getMaxExclusiveTime() : apiTime.getMaxInclusiveTime());
		row.createCell(currentCell++).setCellValue(exclusiveMode ? apiTime.getMinExclusiveTime() : 
													apiTime.getMinInclusiveTime());
		row.createCell(currentCell++).setCellValue(exclusiveMode ? 
											apiTime.getExclusiveFirstExecutionTime(): 
											apiTime.getInclusiveFirstExecutionTime());
		row.createCell(currentCell++).setCellValue(exclusiveMode ? 
											apiTime.getExclusiveLastExecutionTime(): 
											apiTime.getInclusiveLastExecutionTime());
	}

	private short addDoubleCell(HSSFRow row, short currentCell, double value) {
		HSSFCell cell = row.createCell(currentCell++);
		cell.setCellValue(value);
		cell.setCellStyle(defaultStyleForDouble);
		return currentCell;
	}

	private void writeAPISummaryHeader(HSSFRow row, boolean exclusive) {
		short currentCell = 0;
		addColumnHeadingCell(row, currentCell++, "Sl. No");
		addColumnHeadingCell(row, currentCell++, "Operation Name");
		addColumnHeadingCell(row, currentCell++, "Count");
		addColumnHeadingCell(row, currentCell++, "Total Time");
		addColumnHeadingCell(row, currentCell++, "Avg.");
		addColumnHeadingCell(row, currentCell++, "Adj. Avg.");
		addColumnHeadingCell(row, currentCell++, "Max");
		addColumnHeadingCell(row, currentCell++, "Min");
		addColumnHeadingCell(row, currentCell++, "First");
		addColumnHeadingCell(row, currentCell++, "Last");
	}

	private HSSFCell addColumnHeadingCell(HSSFRow row, short currentCell, String headingText) {
		HSSFCell headingCell = row.createCell(currentCell++);
		headingCell.setCellValue(headingText);
		headingCell.setCellStyle(columnHeadingStyle);
		return headingCell;
	}

	private int writeSQLStatistics(SqlStatistics[] sqlStatistics, HSSFSheet sheet1, 
												int currentRow, PerformanceDataSnapshot snapShot) {
		for (int i = 0; i < sqlStatistics.length; i++) {
			writeJDBCDataRow(sheet1.createRow(currentRow++), sqlStatistics[i],
					i + 1, snapShot);
		}
		return currentRow;
	}

	private void writeJDBCDataRow(HSSFRow sqlStatisticsRow, SqlStatistics sqlStatistic, int rowNo,
															PerformanceDataSnapshot snapShot) {
		short currentCell = 0;
		sqlStatisticsRow.createCell(currentCell++).setCellValue(rowNo);
		addSqlCell(sqlStatisticsRow, currentCell++, sqlStatistic.getSql());
		currentCell = addDoubleCell(sqlStatisticsRow, currentCell, 
																sqlStatistic.getAvgExecuteTime());
		currentCell = addDoubleCell(sqlStatisticsRow, currentCell, 
																sqlStatistic.getAvgPrepareTime());
//		currentCell = addDoubleCell(sqlStatisticsRow, currentCell, 
//															sqlStatistic.getAverageTimePerFetch());
		sqlStatisticsRow.createCell(currentCell++).setCellValue(sqlStatistic.getNoOfExecutes());
		sqlStatisticsRow.createCell(currentCell++).setCellValue(sqlStatistic.getMaxExecuteTime());
		sqlStatisticsRow.createCell(currentCell++).setCellValue(sqlStatistic.getMinExecuteTime());
		sqlStatisticsRow.createCell(currentCell++).setCellValue(sqlStatistic.getFirstExecuteTime());
		sqlStatisticsRow.createCell(currentCell++).setCellValue(sqlStatistic.getLastExecuteTime());
//		sqlStatisticsRow.createCell(currentCell++).setCellValue(
//				sqlStatistic.getAverageNoOfRowsPerFetch());
//		sqlStatisticsRow.createCell(currentCell++).setCellValue(
//				sqlStatistic.getMinRowsFetched());
	}

	private HSSFCell addSqlCell(HSSFRow row, short currentCell, String sql) {
		HSSFCell sqlCell = row.createCell(currentCell);
		sqlCell.setCellValue(sql);
		return sqlCell;
	}

	private void writeJDBCHeader(HSSFRow headingRow) {
		short currentCell = 0;
		addColumnHeadingCell(headingRow, currentCell++, "Sl. No");
		addColumnHeadingCell(headingRow, currentCell++, "SQL Query");
		addColumnHeadingCell(headingRow, currentCell++, "Avg. Exec Time");
		addColumnHeadingCell(headingRow, currentCell++, "Avg. Prepare Time");
		addColumnHeadingCell(headingRow, currentCell++, "Count");
		addColumnHeadingCell(headingRow, currentCell++, "Max");
		addColumnHeadingCell(headingRow, currentCell++, "Min");
		addColumnHeadingCell(headingRow, currentCell++, "First");
		addColumnHeadingCell(headingRow, currentCell++, "Last");
	}
}
