import javax.swing.*;
import java.awt.event.*;
import java.rmi.*;
import java.net.*;

public class Client extends JFrame{

	private ServerInterface rem;
	private int id;

	JFrame frame=new JFrame();
	JButton b1=new JButton("Enter");
	JTextField feild=new JTextField("192.168.0.184");
	JTextField feild2=new JTextField("");
	JLabel lab=new JLabel("IP address: ");
	JLabel lab2=new JLabel("Username:");

	private Client(){
		frame.setLayout(null);
		frame.add(b1);
		frame.add(feild);
		frame.add(feild2);
		frame.add(lab);
		frame.add(lab2);
		
		b1.setBounds(130,80,80,20);
		feild.setBounds(130,25,100,20);
		feild2.setBounds(130,45,100,20);
		lab.setBounds(60,25,80,15);
		lab2.setBounds(60,45,80,15);

		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			String host=feild.getText();
			String name=feild2.getText();
			if(name.equals("") | host.equals(""))
			{
				frame.setTitle("Empty fields!");
				return;		
			}
			try{
			    	rem = (ServerInterface)Naming.lookup("//"+host+"/obj");
					id = rem.EnterGame(name);
			}
			catch(RemoteException re){
				re.printStackTrace();
			}
			catch(NotBoundException nbe){
				nbe.printStackTrace();
			}
			catch (MalformedURLException mue){
				mue.printStackTrace();
			}

			new GUI(rem,host,name,id).setVisible(true);
			frame.setVisible(false);
			}
		});
		frame.setSize(300,150);
		frame.setDefaultCloseOperation(1);
		frame.setLocation(400,300);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setVisible(true);
	}
		
	public static void main(String args[]) {
			 new Client();
	}
}