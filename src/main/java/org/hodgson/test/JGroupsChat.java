package org.hodgson.test;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author <a href="ralph.hodgson@pressassociation.com">Ralph Hodgson</a>
 * @since 03/06/2014 15:08
 */
public class JGroupsChat extends ReceiverAdapter
{
    static
    {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("jgroups.tcpping.initial_hosts", "10.253.197.203[7800], 10.253.197.68[7800]");
    }

    JChannel channel;
    String user_name = System.getProperty("user.name", "n/a");

    private void start()
            throws Exception
    {
        channel = new JChannel("tcp.xml");
        channel.setReceiver(this);
        channel.connect("JGroupsChat");

        // Loop til we don't want to talk anymore.
        eventLoop();
        channel.close();
    }

    private void eventLoop()
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        {
            try
            {
                System.out.print("> ");
                System.out.flush();
                String line = in.readLine().toLowerCase();
                if (line.startsWith("quit") || line.startsWith("exit"))
                {
                    break;
                }
                line = "[" + user_name + "] " + line;
                Message msg = new Message(null, null, line);
                channel.send(msg);
            }
            catch (Exception e)
            {
            }
        }
    }

    public void viewAccepted(View view)
    {
        System.out.println("** view: " + view);
    }

    public void receive(Message msg)
    {
        System.out.println(msg.getSrc() + ": " + msg.getObject());
    }

    public static void main(String[] args)
            throws Exception
    {
        new JGroupsChat().start();
    }
}