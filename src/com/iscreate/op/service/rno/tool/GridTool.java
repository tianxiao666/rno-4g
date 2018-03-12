package com.iscreate.op.service.rno.tool;

import java.util.ArrayList;
import java.util.List;

import com.iscreate.op.service.rno.vo.Grid;


public class GridTool {

	
	/**
	 * 将指定的区域划分成指定行、列的值
	 * @param width
	 * @param height
	 * @param row
	 * @param col
	 * @return
	 */
	public static List<List<Grid>> splitGridByNum(double width,double height,int row,int col){
		double colStep=width/col;
		double rowStep=height/row;
		
		return splitGridByStep(width,height,rowStep,colStep);
	}
	
	/**
	 * 将指定的区域划分成按照指定行步长、列步长的形式
	 * @param width
	 * @param height
	 * @param rowStep
	 * @param colStep
	 * @return
	 */
	public static List<List<Grid>> splitGridByStep(double width,double height,double rowStep,double colStep){
		
		double startX=0,startY=0;
		List<List<Grid>> result=new ArrayList<List<Grid>>();
		List<Grid> rows=null;
		Grid grid=null;
		for(startY=0;startY<height;startY+=rowStep){
			rows=new ArrayList<Grid>();
			for(startX=0;startX<width;startX+=colStep){
				grid=new Grid();
				grid.setLeftX(startX);
				grid.setUpY(startY);
				grid.setRightX(startX+colStep);
				grid.setBottomY(startY+rowStep);
				grid.setValue(0);
				rows.add(grid);
			}
			result.add(rows);
		}
		return result;
	}
	
	public static void main(String[] args) {
		List<List<Grid>> grids=splitGridByNum(400, 400, 100, 100);
		System.out.println(grids);
	}
}
