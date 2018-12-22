package com.jneaglel.xlstool.bjmxfl.core.control;

import java.io.File;
import java.util.Collection;

import com.dwarfeng.dutil.develop.backgr.Task;

/**
 * 程序的动作处理器。
 * 
 * @author DwArFeng
 * @since 0.0.0-alpha
 */
public interface ActionManager {

	// --------------------------------------------程序控制--------------------------------------------
	/**
	 * 启动程序。
	 * 
	 * <p>
	 * 通过该方法使用指定的参数启动程序。该方法只允许调用一次，多次调用会抛出异常。
	 * 
	 * @param args 指定的参数。
	 * @throws IllegalStateException 程序已经启动。
	 * @throws NullPointerException  指定的入口参数为 <code> null </code>。
	 */
	public void start(String[] args) throws IllegalStateException, NullPointerException;

	public void exit() throws IllegalStateException;

	// --------------------------------------------程序动作--------------------------------------------
	/**
	 * 
	 * @param task
	 * @throws NullPointerException
	 */
	public void submit(Task task) throws NullPointerException;

	/**
	 * 
	 * @param files
	 * @throws NullPointerException
	 */
	public void setFiles2Import(Collection<File> files) throws NullPointerException;

	/**
	 * 
	 * @param dir
	 * @throws NullPointerException
	 */
	public void export(File dir) throws NullPointerException, IllegalArgumentException;

	// --------------------------------------------日志输出--------------------------------------------
	/**
	 * 
	 * @param message
	 * @throws NullPointerException
	 */
	public void trace(String message) throws NullPointerException;

	/**
	 * 
	 * @param message
	 * @throws NullPointerException
	 */
	public void debug(String message) throws NullPointerException;

	/**
	 * 
	 * @param message
	 * @throws NullPointerException
	 */
	public void info(String message) throws NullPointerException;

	/**
	 * 
	 * @param message
	 * @throws NullPointerException
	 */
	public void warn(String message) throws NullPointerException;

	/**
	 * 
	 * @param message
	 * @param t
	 * @throws NullPointerException
	 */
	public void warn(String message, Throwable t) throws NullPointerException;

	/**
	 * 
	 * @param message
	 * @param t
	 * @throws NullPointerException
	 */
	public void error(String message, Throwable t) throws NullPointerException;

	/**
	 * 
	 * @param message
	 * @param t
	 * @throws NullPointerException
	 */
	public void fatal(String message, Throwable t) throws NullPointerException;

}
