package app;

import app.Command.SendingMode;
import app.Singleton.CommandBagSingleton;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.server.ServerEndpoint;
import javax.websocket.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/chat")
public class WSHandlerAnnotation {
    private static final Set<WSHandlerAnnotation> connections = new CopyOnWriteArraySet<>();
    
    private Session session;
    private StringBuilder out;

    {
        out = new StringBuilder();
    }

    @OnOpen
    public void start(Session session) {
        this.session = session;
        connections.add(this);
    }

    @OnClose
    public void end() {
        connections.remove(this);
    }


    @OnMessage
    public void incoming(String message) {
        SendingMode mode = null;
        try {
            out.delete(0,out.length());
            JSONObject obj = new JSONObject(message);
            String pageName = obj.getString("cmd");
            JSONObject data = obj.getJSONObject("data");
            mode = CommandBagSingleton.getInstance().GetCommand(pageName).Execute(data, out);
        } catch (Exception e) {
            out.delete(0,out.length());
            JSONObject err = new JSONObject();
            try {
                JSONObject data = new JSONObject();
                err.put("err", data);
                data.put("message", e.toString());
//                JSONArray arr = new JSONArray();
//                data.put("stack", arr);
//                StackTraceElement[] stack = e.getStackTrace();
//                for (int i = 0; i < stack.length; i++) {
//                    arr.put(stack[i].toString());
//                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            out.append(err.toString());
            mode = SendingMode.TO_SENDER;
        }

        try {
            if (mode == SendingMode.TO_SENDER) {
                session.getBasicRemote().sendText(out.toString());
            }
            else if (mode == SendingMode.TO_ALL) {
                broadcast(out.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String msg) {
        for (WSHandlerAnnotation client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
            }
        }
    }
}