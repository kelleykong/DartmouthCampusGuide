package edu.dartmouth.cs.dcampus.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import edu.dartmouth.cs.dcampus.gcm.Message;
import edu.dartmouth.cs.dcampus.gcm.Sender;
import edu.dartmouth.cs.dcampus.server.data.PostEntity;
import edu.dartmouth.cs.dcampus.server.data.RegDatastore;




/**
 * Servlet that adds a new message to all registered devices.
 * <p>
 * This servlet is used just by the browser (i.e., not device).
 */
@SuppressWarnings("serial")
public class SendMessagesServlet extends HttpServlet {

	private static final int MAX_RETRY = 5;

	/**
	 * Processes the request to add a new message.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		System.out.println("sendmessagesservlet post");
		List<String> devices = RegDatastore.getDevices();

		Message message = new Message(devices);
		message.addData("message", "delete");
		message.addData("id", req.getParameter(PostEntity.FIELD_NAME_ID));
		System.out.println(req.getParameter(PostEntity.FIELD_NAME_ID));
		System.out.println(req.getParameter("type"));
		
		// Have to hard-coding the API key when creating the Sender
		Sender sender = new Sender(Globals.GCMAPIKEY);
		// Send the message to device, at most retrying MAX_RETRY times
		sender.send(message, MAX_RETRY);

		resp.sendRedirect("/query.do");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}
}
