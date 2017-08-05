package com.rledford.impinj.tools;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.rledford.impinj.tools.ProvisionHandler.ProvisionParams;

public class MainWindow implements CommandListner{

	protected Shell shlImpinjReaderTool;
	private Text txtHost;
	private Text txtPort;
	private Text txtUsername;
	private Label lblUsername;
	private Text txtPassword;
	private Label lblPassword;
	private Text txtMessages;
	private Button btnSend;
	
	private CommandHandler commandHandler = new CommandHandler();
	private Text txtCommands;
	private Label lblCommands;
	private Button btnSaveMessges;
	private CTabFolder tabFolder;
	private CTabItem tbtmNetwork;
	private Composite composite;
	private Group grpStaticIp;
	private Group grpInfo;
	private Group grpDynamicIp;
	private Button btnEnableDynamicIp;
	private Button bntNetSummary;
	private Button btnNetNTP;
	private Button btnNetDHCP;
	private Button bntNetFTP;
	private Button btnNetSFTP;
	private CTabItem tbtmNtp;
	private Composite composite_1;
	private Group grpDynamicServers;
	private Button btnNTPDynamicServersEnable;
	private Button btnNTPDyanmicServersDisable;
	private Group grpServers;
	private Label lblIpAddr_1;
	private Button btnNTPServersAdd;
	private Text txtNTPServerIp;
	private Button btnNTPServersRemove;
	private Button btnNTPServersRemoveAll;
	private Group grpNtpService;
	private Button btnNTPEnable;
	private Button btnNTPDisable;
	private Label lblIpAddr;
	private Label lblMask;
	private Text txtStaticIp;
	private Text txtStaticMask;
	private Label lblGateway;
	private Text txtStaticGateway;
	private Label lblBroadcast;
	private Text txtStaticBroadcast;
	private Button btnSetStaticIp;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlImpinjReaderTool.open();
		shlImpinjReaderTool.layout();
		while (!shlImpinjReaderTool.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlImpinjReaderTool = new Shell();
		shlImpinjReaderTool.setMinimumSize(new Point(450, 380));
		shlImpinjReaderTool.setSize(700, 500);
		shlImpinjReaderTool.setText("Impinj Reader Tool");
		
		shlImpinjReaderTool.setLayout(new FormLayout());
		
		txtMessages = new Text(shlImpinjReaderTool, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		FormData fd_txtMessages = new FormData();
		txtMessages.setLayoutData(fd_txtMessages);
		txtMessages.setEditable(false);
		
		btnSaveMessges = new Button(shlImpinjReaderTool, SWT.NONE);
		btnSaveMessges.addSelectionListener(new SelectionAdapter() {
			Shell shell = shlImpinjReaderTool;
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(shell, SWT.SAVE);
				String rVal = dialog.open();
				if (rVal != null && rVal.trim().length() > 0) {
					try {
						PrintWriter writer = new PrintWriter(rVal);
						writer.write(txtMessages.getText());
						writer.close();
					}
					catch (SecurityException se) {
						System.out.print(se);
					}
					catch (FileNotFoundException fnf) {
						System.out.println(fnf);
					}
				}
			}
		});
		fd_txtMessages.right = new FormAttachment(btnSaveMessges, 0, SWT.RIGHT);
		fd_txtMessages.bottom = new FormAttachment(btnSaveMessges, -6);
		FormData fd_btnSaveMessges = new FormData();
		fd_btnSaveMessges.right = new FormAttachment(100, -10);
		fd_btnSaveMessges.bottom = new FormAttachment(100, -10);
		btnSaveMessges.setLayoutData(fd_btnSaveMessges);
		btnSaveMessges.setText("Save Messges");
		
		Label lblHost = new Label(shlImpinjReaderTool, SWT.RIGHT);
		fd_txtMessages.top = new FormAttachment(lblHost, 0, SWT.TOP);
		FormData fd_lblHost = new FormData();
		fd_lblHost.top = new FormAttachment(0, 10);
		lblHost.setLayoutData(fd_lblHost);
		lblHost.setText("Host");
		
		txtHost = new Text(shlImpinjReaderTool, SWT.BORDER);
		fd_lblHost.right = new FormAttachment(txtHost, -6);
		FormData fd_txtHost = new FormData();
		fd_txtHost.left = new FormAttachment(0, 72);
		fd_txtHost.top = new FormAttachment(0, 7);
		txtHost.setLayoutData(fd_txtHost);
		txtHost.setText("192.168.1.8");
		
		lblUsername = new Label(shlImpinjReaderTool, SWT.NONE);
		FormData fd_lblUsername = new FormData();
		fd_lblUsername.left = new FormAttachment(0, 10);
		fd_lblUsername.top = new FormAttachment(lblHost, 11);
		lblUsername.setLayoutData(fd_lblUsername);
		lblUsername.setText("Username");
		
		txtUsername = new Text(shlImpinjReaderTool, SWT.BORDER);
		FormData fd_txtUsername = new FormData();
		fd_txtUsername.right = new FormAttachment(txtHost, 0, SWT.RIGHT);
		fd_txtUsername.top = new FormAttachment(lblUsername, -3, SWT.TOP);
		fd_txtUsername.left = new FormAttachment(txtHost, 0, SWT.LEFT);
		txtUsername.setLayoutData(fd_txtUsername);
		txtUsername.setText("root");
		
		lblCommands = new Label(shlImpinjReaderTool, SWT.NONE);
		FormData fd_lblCommands = new FormData();
		fd_lblCommands.top = new FormAttachment(lblUsername, 9);
		fd_lblCommands.right = new FormAttachment(lblHost, 0, SWT.RIGHT);
		lblCommands.setLayoutData(fd_lblCommands);
		lblCommands.setText("Command");
		
		txtCommands = new Text(shlImpinjReaderTool, SWT.BORDER);
		FormData fd_txtCommands = new FormData();
		fd_txtCommands.left = new FormAttachment(txtHost, 0, SWT.LEFT);
		fd_txtCommands.right = new FormAttachment(lblCommands, 285, SWT.RIGHT);
		fd_txtCommands.top = new FormAttachment(lblCommands, -3, SWT.TOP);
		txtCommands.setLayoutData(fd_txtCommands);
		
		txtPassword = new Text(shlImpinjReaderTool, SWT.BORDER | SWT.PASSWORD);
		FormData fd_txtPassword = new FormData();
		fd_txtPassword.top = new FormAttachment(lblUsername, -3, SWT.TOP);
		txtPassword.setLayoutData(fd_txtPassword);
		txtPassword.setText("impinj");
		
		lblPassword = new Label(shlImpinjReaderTool, SWT.NONE);
		fd_txtPassword.right = new FormAttachment(lblPassword, 106, SWT.RIGHT);
		fd_txtPassword.left = new FormAttachment(lblPassword, 6);
		FormData fd_lblPassword = new FormData();
		fd_lblPassword.left = new FormAttachment(lblUsername, 122);
		fd_lblPassword.top = new FormAttachment(lblUsername, 0, SWT.TOP);
		lblPassword.setLayoutData(fd_lblPassword);
		lblPassword.setAlignment(SWT.RIGHT);
		lblPassword.setText("Password");
		
		Label lblPort = new Label(shlImpinjReaderTool, SWT.RIGHT);
		fd_txtHost.right = new FormAttachment(lblPort, -34);
		FormData fd_lblPort = new FormData();
		fd_lblPort.top = new FormAttachment(lblHost, 0, SWT.TOP);
		fd_lblPort.right = new FormAttachment(lblPassword, 0, SWT.RIGHT);
		lblPort.setLayoutData(fd_lblPort);
		lblPort.setText("Port");
		
		txtPort = new Text(shlImpinjReaderTool, SWT.BORDER);
		FormData fd_txtPort = new FormData();
		fd_txtPort.right = new FormAttachment(lblPort, 81, SWT.RIGHT);
		fd_txtPort.left = new FormAttachment(lblPort, 6);
		fd_txtPort.top = new FormAttachment(lblHost, -3, SWT.TOP);
		txtPort.setLayoutData(fd_txtPort);
		txtPort.setText("22");
		
		btnSend = new Button(shlImpinjReaderTool, SWT.NONE);
		fd_txtMessages.left = new FormAttachment(btnSend, 6);
		FormData fd_btnSend = new FormData();
		fd_btnSend.left = new FormAttachment(txtCommands, 6);
		fd_btnSend.right = new FormAttachment(100, -271);
		fd_btnSend.top = new FormAttachment(lblCommands, -7, SWT.TOP);
		btnSend.setLayoutData(fd_btnSend);
		btnSend.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams());
			}
		});
		btnSend.setText("Send");
		
		tabFolder = new CTabFolder(shlImpinjReaderTool, SWT.BORDER);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.right = new FormAttachment(lblCommands, 421);
		fd_tabFolder.bottom = new FormAttachment(btnSaveMessges, 0, SWT.BOTTOM);
		fd_tabFolder.top = new FormAttachment(btnSend, 1);
		fd_tabFolder.left = new FormAttachment(lblCommands, 6, SWT.LEFT);
		tabFolder.setLayoutData(fd_tabFolder);
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		tbtmNetwork = new CTabItem(tabFolder, SWT.NONE);
		tbtmNetwork.setText("Network");
		
		composite = new Composite(tabFolder, SWT.NONE);
		tbtmNetwork.setControl(composite);
		composite.setLayout(new FormLayout());
		
		grpStaticIp = new Group(composite, SWT.NONE);
		grpStaticIp.setLayout(new GridLayout(4, false));
		FormData fd_grpStaticIp = new FormData();
		fd_grpStaticIp.top = new FormAttachment(0, 10);
		fd_grpStaticIp.left = new FormAttachment(0, 10);
		fd_grpStaticIp.bottom = new FormAttachment(0, 123);
		fd_grpStaticIp.right = new FormAttachment(0, 399);
		grpStaticIp.setLayoutData(fd_grpStaticIp);
		grpStaticIp.setText("Static IP");
		
		grpInfo = new Group(composite, SWT.NONE);
		grpInfo.setLayout(new GridLayout(5, false));
		FormData fd_grpInfo = new FormData();
		fd_grpInfo.left = new FormAttachment(0, 10);
		fd_grpInfo.bottom = new FormAttachment(100, -80);
		fd_grpInfo.right = new FormAttachment(100, -10);
		grpInfo.setLayoutData(fd_grpInfo);
		grpInfo.setText("Info");
		
		grpDynamicIp = new Group(composite, SWT.NONE);
		fd_grpInfo.top = new FormAttachment(grpDynamicIp, 6);
		
		bntNetSummary = new Button(grpInfo, SWT.NONE);
		bntNetSummary.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.SHOW_NET_SUMMARY));
			}
		});
		bntNetSummary.setText("Summary");
		
		btnNetNTP = new Button(grpInfo, SWT.NONE);
		btnNetNTP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.SHOW_NET_NTP));
			}
		});
		btnNetNTP.setText("NTP");
		
		btnNetDHCP = new Button(grpInfo, SWT.NONE);
		btnNetDHCP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.SHOW_NET_DHCP));
			}
		});
		btnNetDHCP.setText("DHCP");
		
		bntNetFTP = new Button(grpInfo, SWT.NONE);
		bntNetFTP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.SHOW_NET_FTP));
			}
		});
		bntNetFTP.setText("FTP");
		
		btnNetSFTP = new Button(grpInfo, SWT.NONE);
		btnNetSFTP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.SHOW_NET_SFTP));
			}
		});
		btnNetSFTP.setText("SFTP");
		grpDynamicIp.setText("Dynamic IP");
		grpDynamicIp.setLayout(new GridLayout(1, false));
		FormData fd_grpDynamicIp = new FormData();
		fd_grpDynamicIp.top = new FormAttachment(grpStaticIp, 6);
		fd_grpDynamicIp.left = new FormAttachment(grpStaticIp, 0, SWT.LEFT);
		
		lblIpAddr = new Label(grpStaticIp, SWT.NONE);
		lblIpAddr.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIpAddr.setText("IP Addr");
		
		txtStaticIp = new Text(grpStaticIp, SWT.BORDER);
		txtStaticIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblMask = new Label(grpStaticIp, SWT.NONE);
		lblMask.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMask.setText("Mask");
		
		txtStaticMask = new Text(grpStaticIp, SWT.BORDER);
		txtStaticMask.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblGateway = new Label(grpStaticIp, SWT.NONE);
		lblGateway.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGateway.setText("Gateway");
		
		txtStaticGateway = new Text(grpStaticIp, SWT.BORDER);
		txtStaticGateway.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lblBroadcast = new Label(grpStaticIp, SWT.NONE);
		lblBroadcast.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBroadcast.setText("Broadcast");
		
		txtStaticBroadcast = new Text(grpStaticIp, SWT.BORDER);
		txtStaticBroadcast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(grpStaticIp, SWT.NONE);
		new Label(grpStaticIp, SWT.NONE);
		new Label(grpStaticIp, SWT.NONE);
		
		btnSetStaticIp = new Button(grpStaticIp, SWT.NONE);
		btnSetStaticIp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ip = txtStaticIp.getText();
				String mask = txtStaticMask.getText();
				String gateway = txtStaticGateway.getText();
				String bcast = txtStaticBroadcast.getText();
				String command = CommandHelper.SET_STATIC_IP
					.replace("{ip}", ip)
					.replace("{mask}", mask)
					.replace("{gateway}", gateway)
					.replace("{bcast}", bcast);
				commandHandler.send(createCommandParams(command));
			}
		});
		btnSetStaticIp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSetStaticIp.setText("Set");
		fd_grpDynamicIp.right = new FormAttachment(0, 110);
		grpDynamicIp.setLayoutData(fd_grpDynamicIp);
		
		btnEnableDynamicIp = new Button(grpDynamicIp, SWT.NONE);
		btnEnableDynamicIp.setText("Enable");
		
		tbtmNtp = new CTabItem(tabFolder, SWT.NONE);
		tbtmNtp.setText("NTP");
		
		composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmNtp.setControl(composite_1);
		composite_1.setLayout(new FormLayout());
		
		grpDynamicServers = new Group(composite_1, SWT.NONE);
		grpDynamicServers.setLayout(new GridLayout(2, false));
		FormData fd_grpDynamicServers = new FormData();
		fd_grpDynamicServers.bottom = new FormAttachment(0, 75);
		fd_grpDynamicServers.right = new FormAttachment(100, -10);
		grpDynamicServers.setLayoutData(fd_grpDynamicServers);
		grpDynamicServers.setText("Dynamic Servers");
		
		btnNTPDynamicServersEnable = new Button(grpDynamicServers, SWT.NONE);
		btnNTPDynamicServersEnable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.ENABLE_NTP_DYNAMIC_SERVERS));
			}
		});
		btnNTPDynamicServersEnable.setText("Enable");
		
		btnNTPDyanmicServersDisable = new Button(grpDynamicServers, SWT.NONE);
		btnNTPDyanmicServersDisable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.DISABLE_NTP_DYNAMIC_SERVERS));
			}
		});
		btnNTPDyanmicServersDisable.setText("Disable");
		
		grpServers = new Group(composite_1, SWT.NONE);
		grpServers.setText("Servers");
		grpServers.setLayout(new GridLayout(4, false));
		FormData fd_grpServers = new FormData();
		fd_grpServers.bottom = new FormAttachment(grpDynamicServers, 121, SWT.BOTTOM);
		fd_grpServers.top = new FormAttachment(grpDynamicServers, 6);
		fd_grpServers.left = new FormAttachment(0, 10);
		fd_grpServers.right = new FormAttachment(100, -10);
		grpServers.setLayoutData(fd_grpServers);
		
		lblIpAddr_1 = new Label(grpServers, SWT.NONE);
		lblIpAddr_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIpAddr_1.setText("IP Addr");
		
		txtNTPServerIp = new Text(grpServers, SWT.BORDER);
		txtNTPServerIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnNTPServersAdd = new Button(grpServers, SWT.NONE);
		btnNTPServersAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ip = txtNTPServerIp.getText();
				String command = CommandHelper.ADD_NTP_IP
						.replace("{ip}", ip);
				commandHandler.send(createCommandParams(command));
			}
		});
		btnNTPServersAdd.setText("Add");
		
		btnNTPServersRemove = new Button(grpServers, SWT.NONE);
		btnNTPServersRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ip = txtNTPServerIp.getText();
				String command = CommandHelper.DEL_NTP_IP
						.replace("{ip}", ip);
				commandHandler.send(createCommandParams(command));
			}
		});
		btnNTPServersRemove.setText("Remove");
		new Label(grpServers, SWT.NONE);
		
		btnNTPServersRemoveAll = new Button(grpServers, SWT.NONE);
		btnNTPServersRemoveAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.DEL_ALL_NTP_SERVERS));
			}
		});
		btnNTPServersRemoveAll.setText("Remove All Servers");
		new Label(grpServers, SWT.NONE);
		new Label(grpServers, SWT.NONE);
		
		grpNtpService = new Group(composite_1, SWT.NONE);
		fd_grpDynamicServers.left = new FormAttachment(grpNtpService, 29);
		fd_grpDynamicServers.top = new FormAttachment(0, 10);
		grpNtpService.setText("NTP Service");
		grpNtpService.setLayout(new GridLayout(2, false));
		FormData fd_grpNtpService = new FormData();
		fd_grpNtpService.bottom = new FormAttachment(0, 75);
		fd_grpNtpService.top = new FormAttachment(0, 10);
		fd_grpNtpService.right = new FormAttachment(100, -219);
		fd_grpNtpService.left = new FormAttachment(0, 10);
		grpNtpService.setLayoutData(fd_grpNtpService);
		
		btnNTPEnable = new Button(grpNtpService, SWT.NONE);
		btnNTPEnable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.ENABLE_NTP));
			}
		});
		btnNTPEnable.setText("Enable");
		
		btnNTPDisable = new Button(grpNtpService, SWT.NONE);
		btnNTPDisable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				commandHandler.send(createCommandParams(CommandHelper.DISABLE_NTP));
			}
		});
		btnNTPDisable.setText("Disable");
		shlImpinjReaderTool.setTabList(new Control[]{txtHost, txtPort, txtUsername, txtPassword, txtCommands});
		
		this.shlImpinjReaderTool.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				// TODO Auto-generated method stub
				System.out.println("closing window");
			}
		});

		tbtmNetwork.getParent().setSelection(0);
		ProvisionHandler.disableSSLCertificateChecking();
		
		ProvisionParams params = new ProvisionParams();
		byte[] cert = ProvisionHandler.loadPemFile("/Users/rledford/eclipse-workspace/impinj-reader-config/localItemSense.pem");
		params.readerIp = "192.168.1.8";
		params.itemSenseIp = "192.168.1.19";
		params.agentId = "SpeedwayR-12-03-62";
		params.apiKey = "6ea9d358-d9ff-4dcc-a622-d44e461906bf";
		params.pathToPemFile = "/Users/rledford/eclipse-workspace/impinj-reader-config/localItemSense.pem";
		ProvisionHandler.provision(params);
	}
	
	/*
	 * returns params with the command set with whatever is in the Command text box
	 */
	private CommandHandler.Params createCommandParams() {
		return new CommandHandler.Params(
				txtHost.getText(),
				txtUsername.getText(),
				txtPassword.getText(),
				Integer.parseInt(txtPort.getText()),
				txtCommands.getText(), this);
	}
	
	/*
	 * returns params with the command set to the value of the command arg 
	 */
	private CommandHandler.Params createCommandParams(String command) {
		return new CommandHandler.Params(
				txtHost.getText(),
				txtUsername.getText(),
				txtPassword.getText(),
				Integer.parseInt(txtPort.getText()),
				command, this);
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println("SUCCESS: " + message);
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				txtMessages.append(message+"\n");
			}
		});
	}
}
