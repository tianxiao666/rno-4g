package com.iscreate.op.service.rno.job.vmhostctrl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;

import com.iscreate.op.service.rno.job.avro.proto.VmCtrlRequest;
import com.iscreate.op.service.rno.job.avro.proto.VmCtrlResponse;
import com.iscreate.op.service.rno.job.avro.proto.VmHostCtrlProtocolAvro;
import com.iscreate.op.service.rno.job.avro.proto.VmInfo;
import com.iscreate.op.service.rno.job.avro.proto.VmInfoList;

public class VmHostCtrlServer {

	private Server server;

	static class VmHostCtrlProtocolAvroImpl implements VmHostCtrlProtocolAvro {

		@Override
		public VmCtrlResponse createVm(VmCtrlRequest request)
				throws AvroRemoteException {

			System.out.println("creatVm,request="+request);
			VmCtrlResponse resp = new VmCtrlResponse();
			if (!VmCtrlCmdType.Create.getCode().equals(
					request.getCmdType().toString())) {
				// 不合适的请求
				resp.setIsAccepted(false);
				resp.setProgress("不是创建虚拟机的指令类型！" + request.getCmdType());
			}
			String scriptFile = "";
			File sf=null;
			try {
				scriptFile = createVmScript(request);
				sf=new File(scriptFile);
//				System.out.println("scriptFilePath="+sf.getAbsolutePath());
				ExecuteShell.executeShellScript(scriptFile);
				resp.setIsAccepted(true);
				resp.setProgress("创建中");

			} catch (IOException e) {
				resp.setIsAccepted(false);
				resp.setProgress("构建脚本失败！");
				e.printStackTrace();
			} catch (InterruptedException e) {
				resp.setIsAccepted(false);
				resp.setIsAccepted(false);
				e.printStackTrace();
			} finally {
//				new File(scriptFile).delete();
			}

			if (resp.getIsAccepted()) {
				// 查询状态
				List<String> cmds = new ArrayList<String>();
				cmds.add("virsh");
				cmds.add("domstate");
				cmds.add(request.getVmName().toString());
				try {
					List<String> results = ExecuteShell.execute(cmds);
					if (results != null && results.size() > 0) {
						if (results.get(0).contains("错误")) {
							resp.setIsAccepted(false);
							resp.setProgress(results.get(0)
									+ "  "
									+ (results.size() >= 2 ? results.get(1)
											: ""));
						} else {
							resp.setProgress(results.get(0));
						}
					} else {
						resp.setProgress("未知状态");
					}
				} catch (IOException e) {
					e.printStackTrace();
					resp.setProgress("未知状态");
				}catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			return resp;
		}

		//
		public String createVmScript(VmCtrlRequest request) throws IOException {
			String scriptFilePath = "script-" + request.getVmName() + "-"
					+ UUID.randomUUID().toString().replaceAll("-", "");
//			System.out.println("创建脚本文件:" + scriptFilePath);
			File scriptFile = new File(scriptFilePath);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(scriptFile)));

			// 需要创建的node类型
			String nodeType = request.getNodeType().toString();

			bw.write("#!/bin/sh");
			bw.newLine();
			bw.write("destpath=/data/vmctrl/vmhome");
			bw.newLine();
			bw.write("baseimgpath=/data/vmctrl/template");
			bw.newLine();
			bw.write("basexmlpath=/data/vmctrl/template");
			bw.newLine();
			bw.write("");
			bw.newLine();
			bw.write("oldimg=$baseimgpath/" + nodeType + ".img");
			bw.newLine();
			bw.write("oldxml=$basexmlpath/" + nodeType + ".xml");
			bw.newLine();

			bw.write("newvmname=" +request.getVmName());
			bw.newLine();

			bw.write("main()");
			bw.newLine();
			bw.write("{");

			bw.newLine();
			bw.write("if [ ! -d \"$destpath\" ]; then ");
			bw.newLine();
			bw.write("  mkdir \"$destpath\"");
			bw.newLine();
			bw.write("fi");
			bw.newLine();
			bw.write("OLDMAC=`cat $oldxml | grep \"mac address\" | cut -d \"'\" -f 2|awk '{if(NR==1){print}}'`");
			bw.newLine();
			bw.write("oldimgfile=`cat $oldxml | grep \"source file\" | cut -d \"'\" -f 2|awk '{if(NR==1){print}}'`");
			bw.newLine();
			bw.write("OLDHOSTNAME=`cat $oldxml | grep \"name\" | cut -d \">\" -f 2 | cut -d \"<\" -f 1|awk '{if(NR==1){print}}'`");
			bw.newLine();
			bw.write("OLDUUID=`cat $oldxml | grep \"uuid\" | cut -d \">\" -f 2 | cut -d \"<\" -f 1|awk '{if(NR==1){print}}'`");
			bw.newLine();
			bw.write("OLDPORT=`cat $oldxml | grep \"'vnc' port=\" | cut -d \"'\" -f 4|awk '{if(NR==1){print}}'`");
			bw.newLine();

			bw.write("createLinux");
			bw.newLine();
			bw.write("}");

			bw.newLine();
			bw.write("modifyInstance(){");
			bw.newLine();
			bw.write("virt-sysprep --hostname $newvmname -d $newvmname");
			bw.newLine();
			
			writeLine(bw,"autoConfScript=/data/vmctrl/autoConfigScript.sh");
			bw.newLine();
			writeLine(bw,"if [ -f \"$autoConfScript\" ];then ");
			writeLine(bw,"sh $autoConfScript $newvmname");
			writeLine(bw,"else");
			writeLine(bw,"virt-sysprep --hostname $newvmname -d $newvmname");
			writeLine(bw,"virt-copy-in /data/vmctrl/conf/ifcfg-eth0 /etc/sysconfig/network-scripts/ -d $newvmname");
			writeLine(bw,"virt-copy-in /data/vmctrl/conf/autofillhostname.sh /opt/script/ -d $newvmname");
			writeLine(bw,"fi");
			
			writeLine(bw,"}");
			bw.newLine();

			bw.newLine();
			bw.write("createLinux()");
			bw.newLine();
			bw.write("{");
			bw.newLine();

			bw.write("NEWMAC=`printf '08:%02X:%02X:%02X:%02X:%02X' $[RANDOM%256] $[RANDOM%256] $[RANDOM%256] $[RANDOM%256] $[RANDOM%256]`");
			bw.newLine();
			bw.write("newimg=$destpath/$newvmname.img");
			bw.newLine();
			bw.write("newxml=$destpath/$newvmname.xml");
			bw.newLine();
			bw.write("NEWHOSTNAME=$newvmname");
			bw.newLine();
			bw.write("NEWUUID=`uuidgen`");
			bw.newLine();
			bw.write("NEWPORT=-1");
			bw.newLine();
			bw.write("qemu-img create -b $oldimg -f qcow2  $newimg");
			bw.newLine();
			writeLine(bw,"chmod a+w  $newimg");
			bw.newLine();
			writeLine(bw,"cp -f $oldxml $newxml");
			writeLine(bw,"chmod a+w  $newxml");
			bw.newLine();
			writeLine(bw,"/bin/sed -i -e \"s@$oldimgfile@$newimg@g\" -e \"s@<name>[ ]*$OLDHOSTNAME[ ]*</name>@<name>$NEWHOSTNAME</name>@g\" -e \"s/$OLDUUID/$NEWUUID/g\" -e \"s/$OLDMAC/$NEWMAC/g\"  -e \"s/$OLDPORT/$NEWPORT/g\" $newxml");

			bw.write("virsh define $newxml");
			bw.newLine();
			bw.write("modifyInstance");
			bw.newLine();
			bw.write("virsh start $NEWHOSTNAME");
			bw.newLine();
			bw.write("}");
			bw.newLine();
			bw.newLine();
			bw.write("main");

			bw.close();

			return scriptFilePath;

		}

		@Override
		public VmInfoList listVm() throws AvroRemoteException {
			List<String> cmds = new ArrayList<String>();
			cmds.add("virsh");
			cmds.add("list");
			cmds.add("--all");
			List<String> results = new ArrayList<String>();
			VmInfoList response = new VmInfoList();
			try {
				results = ExecuteShell.execute(cmds);
//				System.out.println("listvm execute result:" + results);
				List<VmInfo> vms = new ArrayList<VmInfo>();
				if (results != null && results.size() > 0) {
					VmInfo vm = null;
					int startPoi = 3;
					int curPoi = 0;
					for (String line : results) {
						curPoi++;
//						System.out.println("listvm raw msg:" + line);
						if (curPoi < startPoi) {
//							System.out.println("skip...");
							continue;
						}
//						System.out.println("take msg:" + line);
						vm = new VmInfo();
						StringTokenizer st = new StringTokenizer(line, " "); // 使用空字符串分割字符
						List<String> tmp = new ArrayList<String>();
						while (st.hasMoreTokens()) {
							tmp.add(st.nextToken());
						}
						if (tmp.size() < 3) {
//							System.out.println("格式错误！");
							continue;
						}
						vm.setName(tmp.get(1));
						vm.setNodeType("WorkerNode");
						vm.setState(tmp.get(2));
						vm.setToken(tmp.get(1));
						vms.add(vm);
					}
				}
//				System.out.println("vms==" + vms);
				response.setVmlist(vms);
//				System.out.println("just aft set : vmlist:"
//						+ response.getVmlist());
			} catch (IOException e) {
				e.printStackTrace();
				response.setVmlist(null);
			}catch (InterruptedException e1) {
				e1.printStackTrace();
			}

//			System.out.println("before return,vmlist:" + response.getVmlist());
			return response;
		}

		@Override
		public VmCtrlResponse destroyVm(VmCtrlRequest request)
				throws AvroRemoteException {

			List<String> cmds = new ArrayList<String>();
			cmds.add("virsh");
			cmds.add("destroy");
			cmds.add(request.getVmName().toString());
			
			List<String> results = new ArrayList<String>();
			VmCtrlResponse response = new VmCtrlResponse();
			response.setIsAccepted(false);
			response.setProgress(" ");
			try {
				results = ExecuteShell.execute(cmds);
				if (results != null && results.size() > 0) {
					if (results.get(0).contains("成功")
							|| results.get(0).contains("被删除")) {
						response.setIsAccepted(true);
						response.setProgress(results.get(0));
					} else {
						response.setIsAccepted(false);
						response.setProgress(results.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setIsAccepted(false);
				response.setProgress(e.getLocalizedMessage());
			}
			
			//undine 
			cmds.clear();
			cmds.add("virsh");
			cmds.add("undefine");
			cmds.add(request.getVmName().toString());
			try {
				results = ExecuteShell.execute(cmds);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			//remove img,xml
			cmds.clear();
			cmds.add("rm");
			cmds.add("-fr");
			cmds.add("/data/vmctrl/vmhome/"+request.getVmName().toString()+".*");
			System.out.println(cmds);
			try {
				List<String> dstr=ExecuteShell.execute(cmds);
//				System.out.println("delete res:"+dstr);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		public VmCtrlResponse startVm(VmCtrlRequest request)
				throws AvroRemoteException {
			List<String> cmds = new ArrayList<String>();
			cmds.add("virsh");
			cmds.add("start");
			cmds.add(request.getVmName().toString());

			List<String> results = new ArrayList<String>();
			VmCtrlResponse response = new VmCtrlResponse();
			try {
				results = ExecuteShell.execute(cmds);
				if (results != null && results.size() > 0) {
					if (results.get(0).contains("已开始")) {
						response.setIsAccepted(true);
						response.setProgress(results.get(0));
					} else {
						response.setIsAccepted(false);
						response.setProgress(results.toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setIsAccepted(false);
				response.setProgress(e.getLocalizedMessage());
			}
			return response;
		}

		@Override
		public CharSequence getVmState(CharSequence vmName)
				throws AvroRemoteException {
			List<String> cmds = new ArrayList<String>();
			cmds.add("virsh");
			cmds.add("domstate");
			cmds.add(vmName==null?"":vmName.toString());
			List<String> results = new ArrayList<String>();
			try {
				results = ExecuteShell.execute(cmds);
//				System.out.println("getVmState execute result:" + results);
			} catch (IOException e) {
				e.printStackTrace();
			}catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			return results==null?"unknown":results.get(0);
		}
	}

	public void startServer() {
		server = new NettyServer(
				new SpecificResponder(VmHostCtrlProtocolAvro.class,
						new VmHostCtrlProtocolAvroImpl()),
				new InetSocketAddress(65000));
		System.out.println("server started...");
	}

	
	private static void writeLine(BufferedWriter bw,String line) throws IOException{
		bw.write(line);
		bw.newLine();
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Starting server");
		// usually this would be another app, but for simplicity
		VmHostCtrlServer ctrlServer = new VmHostCtrlServer();
		ctrlServer.startServer();
		System.out.println("Server started");

//		 VmCtrlRequest request=new VmCtrlRequest();
//		 request.setNodeType(NodeType.WorkerNode.getCode());
//		 request.setVmName(UUID.randomUUID().toString().replaceAll("-", ""));
//		 VmHostCtrlProtocolAvroImpl impl=new VmHostCtrlProtocolAvroImpl();
//		 System.out.println(impl.createVmScript(request));
	}
}
