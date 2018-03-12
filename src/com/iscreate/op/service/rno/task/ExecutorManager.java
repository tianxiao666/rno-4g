package com.iscreate.op.service.rno.task;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程管理器
 * @author brightming
 *
 */
public class ExecutorManager {

	private static ExecutorService executor;
	
	private	static List<Map<String, Object>> taskThread = new ArrayList<Map<String, Object>>();
	private static ThreadGroup rootThreadGroup = null;
	
	static{
		executor = Executors.newFixedThreadPool(10);
	}
	
	public static void execute(Runnable runnable){
		executor.execute(runnable);
	}
	

	/**
	 * 保存任务线程信息
	 * @param taskId
	 * @param threadId
	 * @author peng.jm
	 */
	public static void saveTaskThreadInfo(long taskId, long threadId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", taskId);
		map.put("threadId", threadId);
		if(taskThread.size() > 0) {
			for (int i = 0; i < taskThread.size(); i++) {
				long taskIdFromMap = Long.parseLong(taskThread.get(i).get("taskId").toString());
				if(taskId == taskIdFromMap) {
					taskThread.set(i, map);
				}
			}
		} else {
			taskThread.add(map);
		}
	}
	
	/**
	 * 通过任务id获取已存的任务线程id
	 * @param taskId
	 * @return	threadId; 返回-1表示不存在属于这个任务的线程
	 * @author peng.jm
	 */
	public static long getThreadIdByTaskId(long taskId) {
		long result = -1;
		if(taskThread.size() > 0 && taskThread != null) {
			for (int i = 0; i < taskThread.size(); i++) {
				long taskIdFromMap = Long.parseLong(taskThread.get(i).get("taskId").toString());
				if(taskId == taskIdFromMap) {
					result = Long.parseLong(taskThread.get(i).get("threadId").toString());
				}
			}
		}
		return result;
	}
	
	/**
	 * 通过任务id删除map中的对应任务线程信息
	 * @param taskId
	 * @author peng.jm
	 */
	public static void deleteTaskThreadInfoByTaskId(long taskId) {
		if(taskThread.size() > 0) {
			for (int i = 0; i < taskThread.size(); i++) {
				long taskIdFromMap = Long.parseLong(taskThread.get(i).get("taskId").toString());
				if(taskIdFromMap == taskId) {
					taskThread.remove(i);
				}
			}
		}
	}
	
	/**
	 * 通过taskId获取记录里面对应的线程interrupt状态
	 * @param taskId
	 * @return	ture表示处于interrupt状态，反之为false
	 * @author peng.jm
	 */
	public static boolean isTheTaskThreadInterrupted(long taskId) {
		boolean result = true;
		long threadId = getThreadIdByTaskId(taskId);
		if(threadId != -1) {
			result = getThread(threadId).isInterrupted();
		}
		return result;
	}
	
	/**
	 * 通过线程id获取当前运行中的线程
	 * @param	id
	 * @return 返回一个线程，如果没有找到就返回null
	 * @author peng.jm
	 */
	public static Thread getThread( final long id ) {
	    final Thread[] threads = getAllThreads( );
	    for ( Thread thread : threads )
	      if ( thread.getId( ) == id )
	        return thread;
	    return null;
	}
	
	/**
	 * 获取当前运行的所有线程
	 * @return	所有线程
	 * @author peng.jm
	 */
	public static Thread[] getAllThreads( ) {
	    final ThreadGroup root = getRootThreadGroup( );
	    final ThreadMXBean thbean =
	      ManagementFactory.getThreadMXBean( );
	    int nAlloc = thbean.getThreadCount( );
	    int n = 0;
	    Thread[] threads = null;
	    do
	    {
	      nAlloc *= 2;
	      threads = new Thread[ nAlloc ];
	      n = root.enumerate( threads, true );
	    } while ( n == nAlloc );
	    return java.util.Arrays.copyOf( threads, n );
	}
	
	/**
	 * 返回当前线程所属的线程组
	 * @author peng.jm
	 */
	public static ThreadGroup getRootThreadGroup() {
		
	    if ( rootThreadGroup != null )
	      return rootThreadGroup;

	    ThreadGroup tg = Thread.currentThread( ).getThreadGroup( );
	    ThreadGroup ptg;
	    while ( (ptg = tg.getParent( )) != null )
	      tg = ptg;
	    rootThreadGroup = tg;
	    return tg;
	}
	
	//测试
	public static void printMap(){
		System.out.println(taskThread);
	}
}
