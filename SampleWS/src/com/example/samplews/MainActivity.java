package com.example.samplews;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.Duration;

import microsoft.exchange.webservices.data.Appointment;
import microsoft.exchange.webservices.data.Attendee;
import microsoft.exchange.webservices.data.AttendeeAvailability;
import microsoft.exchange.webservices.data.AttendeeCollection;
import microsoft.exchange.webservices.data.AttendeeInfo;
import microsoft.exchange.webservices.data.AvailabilityData;
import microsoft.exchange.webservices.data.AvailabilityOptions;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.CalendarEvent;
import microsoft.exchange.webservices.data.CalendarEventDetails;
import microsoft.exchange.webservices.data.CalendarView;
import microsoft.exchange.webservices.data.Contact;
import microsoft.exchange.webservices.data.ContactSchema;
import microsoft.exchange.webservices.data.ContactsFolder;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailAddressCollection;
import microsoft.exchange.webservices.data.EmailAddressDictionary;
import microsoft.exchange.webservices.data.EmailAddressKey;
import microsoft.exchange.webservices.data.ExchangeCredentials;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.FolderId;
import microsoft.exchange.webservices.data.FolderSchema;
import microsoft.exchange.webservices.data.FolderTraversal;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.FreeBusyViewType;
import microsoft.exchange.webservices.data.GetUserAvailabilityResults;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemSchema;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.Mailbox;
import microsoft.exchange.webservices.data.MailboxType;
import microsoft.exchange.webservices.data.MeetingAttendeeType;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.SendInvitationsMode;
import microsoft.exchange.webservices.data.ServiceResponseCollection;
import microsoft.exchange.webservices.data.SuggestionQuality;
import microsoft.exchange.webservices.data.TimeSpan;
import microsoft.exchange.webservices.data.TimeWindow;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WebProxy;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * Accessing Web service in Android application
 * 
 * @author Manoj Behera
 * @version 1.0
 * @since SEP 10 2013
 */
public class MainActivity extends Activity {

	ExchangeService service;
	public static final long HOUR = 3600*1000;
	static int i = 0;
	String[] attendees = new String[12];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = calendar.getTimeZone();
		calendar.setTimeZone(timeZone);
		
		
		service = new ExchangeService();//(ExchangeVersion.Exchange2007_SP1, timeZone);
		
		ExchangeCredentials credentials = new WebCredentials("manoj@vmokshaex.com",
				"Power@1234");//user1@vmex.com//Power@1234
		service.setCredentials(credentials);
		try {
			service.setUrl(new URI("https://10.10.3.168/EWS/exchange.asmx"));//https://mail.ucb.com/ews/exchange.asmx
			service.autodiscoverUrl("manoj@vmokshaex.com");//TestMailAdmin.braexcap013@ucb.com
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		FolderView view = new FolderView(10);
		PropertySet set = new PropertySet(BasePropertySet.IdOnly);
		try {
			set.add(FolderSchema.DisplayName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.setPropertySet(set);
		SearchFilter searchFilter = new SearchFilter.IsGreaterThan(
				FolderSchema.TotalCount, 0);
		view.setTraversal(FolderTraversal.Deep);
		
		try {
			WebProxy value = new WebProxy("proxy.vmoksha.com", 8080);
			service.setWebProxy(value);
			
			
			   service.setTraceEnabled(true);
			   Date start = new Date(new Date().getTime() + 1 * HOUR);
			   Date end = new Date(start.getTime() + 1 * HOUR);
//			   new MyContacts().execute();
//			   new MyMeeting().execute();
//			   new UserAvailable().execute();
			   new RoomLists().execute();
			   /*
			   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   Date startDate = formatter.parse("2015-07-28 12:00:00");
			   Date endDate = formatter.parse("2015-07-28 13:00:00"); 
			
			   Appointment appointment = new Appointment(service);
			   
//			   AttendeeCollection attendeeCollection = appointment.getResources();
//			   attendeeCollection.add("room10@vmokshaex.com");
			   appointment.setStart(startDate);
			   appointment.setEnd(endDate);
			   appointment.setSubject("Test By Anthony Local Server");
			   MessageBody messageBody = new MessageBody("Test By Anthony Local Server");
			   appointment.setBody(messageBody);
			   appointment.setLocation("Bangalore Office");
	
			   appointment.setReminderMinutesBeforeStart(5);
			   AttendeeCollection attendeeCollection1 = appointment.getRequiredAttendees();
//			   for (int i=0;i<attendees.length;i++){
//				   attendeeCollection.add(attendees[i]); 
//			   }
			   EmailAddress mailbox = new EmailAddress("ciby@vmokshaex.com");
			   Attendee attendee = new Attendee(mailbox);
			   attendeeCollection1.add(attendee);//Anthony.YEKULA@ucb.com 
			   EmailAddress bookRoomAddress = new EmailAddress("room10@vmokshaex.com");
			   Mailbox sendInvitationsMode = new Mailbox(bookRoomAddress.getAddress());
			   FolderId destinationFolderId = new FolderId(WellKnownFolderName.Calendar, sendInvitationsMode);
		   
			   appointment.save(destinationFolderId, SendInvitationsMode.SendToAllAndSaveCopy);
			   
			   
//			   Item item = Item.bind(service, appointment.getId(), new PropertySet(ItemSchema.Subject));
			   
//			   ResponseMessage responseMessage = appointment.createForward();
			   Log.i("Manoj Behera", "Saved");*/
			
			/*EmailMessage msg= new EmailMessage(service);
			msg.setSubject("Hello world!");
			msg.setBody(MessageBody.getMessageBodyFromText("Sent using the EWS Managed API."));
            System.out.println("set body to " + msg.getBody().toString());
            msg.getToRecipients().add("mnj.behera1@gmail.com");
            //System.out.println("add recipient: " + msg.getDisplayTo().toString());
            msg.send();*/
			

//			new MyAsynTask().execute();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		try {
//			FindFoldersResults findFolderResults  = service.findFolders(WellKnownFolderName.Root,
//					searchFilter, view);
//			for (Folder folder : findFolderResults) {
//				if (folder instanceof CalendarFolder) {
//					try {
//						System.out.println("Calendar folder: "
//								+ folder.getDisplayName());
//					} catch (ServiceLocalException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					ArrayList<String> calendarNames = new ArrayList<String>();
//					try {
//						calendarNames.add(folder.getDisplayName() + ","
//								+ folder.getId());
//					} catch (ServiceLocalException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		

	}
	
	public class MyAsynTask extends AsyncTask< String,String,String>{

		@Override
		protected String doInBackground(String... params) {
			EmailAddress emailAddress = new EmailAddress("vmokshadelhi@vmokshaex.com");//#India-Bangalore@ucb.com//Building1ConferenceRooms@vmex.com//vmokshabangalore@vmex.com
			try {
				Log.i("", "");
//				Mailbox mailbox = new Mailbox("Building1ConferenceRooms@vmex.com");
//				Date date = service.getPasswordExpirationDate(mailbox);
//				EmailAddressCollection myRoomLists = service.getRoomLists();
				Date newDate = new Date();
				Date oldDate = new Date(new Date().getTime() + 30 * HOUR);
				
				Collection<EmailAddress> addresses = service.getRooms(emailAddress);
				Log.i("Manoj Behera", ""+addresses.size());
				for(EmailAddress roomAddress : addresses){
					Mailbox mailbox = new Mailbox();
					mailbox.setAddress(roomAddress.getAddress());//"Saraswati@ucb.com".toString()
				
					
				    List<AttendeeInfo> attendees = new ArrayList<AttendeeInfo>();
					
				    attendees.add(new AttendeeInfo("ciby@vmokshaex.com"));//, MeetingAttendeeType.Optional, true));//,new AttendeeInfo().setAttendeeType(MeetingAttendeeType.Optional)
//				    attendees.add(new AttendeeInfo("room2@vmex.com"));
				    
				    
				    
				    AvailabilityOptions availabilityOptions = new AvailabilityOptions();
				    availabilityOptions.setGoodSuggestionThreshold(49);
				    availabilityOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
				    availabilityOptions.setMaximumSuggestionsPerDay(2);
				    availabilityOptions.setMeetingDuration(30);
				    availabilityOptions.setMinimumSuggestionQuality(SuggestionQuality.Good);
				    TimeWindow timeWindow = new TimeWindow(newDate, oldDate);
				    availabilityOptions.setDetailedSuggestionsWindow(timeWindow);     
				    availabilityOptions.setRequestedFreeBusyView(FreeBusyViewType.FreeBusy);
				    
				 
				    GetUserAvailabilityResults results = service.getUserAvailability(attendees, timeWindow, AvailabilityData.FreeBusyAndSuggestions, availabilityOptions);
				    Log.i("", "");
				    ServiceResponseCollection<AttendeeAvailability> collection = results.getAttendeesAvailability();
				    for(AttendeeAvailability attendeeAvailability:collection){
				    	Collection<CalendarEvent> calendarEvents = attendeeAvailability.getCalendarEvents();
				    	for(CalendarEvent calendarEvent : calendarEvents){
				    		CalendarEventDetails calendarEventDetails = calendarEvent.getDetails();
				    		
				    	}
				    }
				    Log.i("", "");
					
//					CalendarFolder calendarFolder = CalendarFolder.bind(service, WellKnownFolderName.Calendar,new PropertySet());
//					CalendarView calendarView = new CalendarView(newDate, oldDate);
					
//					FolderId folderID = new FolderId(WellKnownFolderName.Calendar, mailbox);
//					FindItemsResults<Appointment>  ssss =  calendarFolder.findAppointments(calendarView);
//					Log.i("", "");
//					FindItemsResults roomAppts = service.findAppointments(folderID, calendarView);
//					ArrayList<Appointment>  appList = ssss.getItems();
//					for(Appointment appon:appList){
//						try{
//					
//						AttendeeCollection attendeeCollection = appon.getOptionalAttendees();
//						int numberAttendee = attendeeCollection.getCount();
//						Attendee attendee = new Attendee(roomAddress);
//						boolean statusForRoomAddress = attendeeCollection.contains(attendee);
//						String string = appon.getSubject();
//						TimeSpan timeSpan = appon.getDuration();
//						Long long1 = timeSpan.getMinutes();
//						Date date = appon.getEnd();
//						Log.i("Manoj Behera", " "+long1 +" Minutes");
//						}
//						catch(Exception e){
//							e.printStackTrace();
//						}
//					}
//					Log.i("Manoj Behera", "Number :" + (++i) +""+ssss.getItems().toArray().toString());
				}
//				EmailAddressCollection myRoomLists = service.getRoomLists();
//				for(EmailAddress emaAddress : myRoomLists){
				 
				/*Folder rootfolder = Folder.bind(service, WellKnownFolderName.MsgFolderRoot);
				rootfolder.load();
				FolderView view = new FolderView(100);
				for(Folder folder: rootfolder.findFolders(view)){
					String folderName = folder.getDisplayName();
					Log.i("", folderName);
				}*/
				
					Collection<EmailAddress> addressess = service.getRooms(emailAddress);
					for(EmailAddress roomAddress : addressess){
						
						
						EmailAddress myRoomAddress = new EmailAddress("room10@vmokshaex.com");
						Mailbox mailbox = new Mailbox(myRoomAddress.getAddress());
						
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date startDate = formatter.parse("2015-07-30 12:00:00");
						Date endDate = formatter.parse("2015-07-30 12:30:00");                  

						
						CalendarView calendarView = new CalendarView(startDate, endDate,15);
						FolderId folderID = new FolderId(WellKnownFolderName.Calendar,mailbox);
						
						FindItemsResults<Appointment> roomAppts = service.findAppointments(folderID, calendarView);
						
						
						
						ArrayList<Appointment>  appList = roomAppts.getItems();
						for(Appointment appointment : appList){
							String sub = appointment.getSubject();
							Date startTime = appointment.getStart();
							Date endTime = appointment.getEnd();
							TimeSpan timeSpan = appointment.getDuration();
							long timeDur = timeSpan.getMinutes();
//							Long long1 = timeSpan.getMinutes();
							
							Log.i("Manoj Behera", sub +"---starttime: "+startTime+"---endTime: "+endTime+"---Dur: "+timeDur);
						}
						Log.i("", "");
//						ArrayList<String> arrayList = roomAppts.getItems();
					}
//				}
				/*Collection<EmailAddress> addresses = service.getRooms(emailAddress);
				
				for(EmailAddress id: addresses){
					
					Log.i("", "");
					String mailid = id.toString();
					String name = id.getName();
					String ss = id.getAddress();
				}
				Log.i("", "");
				*/
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public class MyMeeting extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			
			   
			   try{
				   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				   Date startDate = formatter.parse("2015-07-29 22:30:00");
				   Date endDate = formatter.parse("2015-07-29 22:45:00"); 
				
				   Appointment appointment = new Appointment(service);
	
				   appointment.setStart(startDate);
				   appointment.setEnd(endDate);
				   appointment.setSubject("10:30 Meeting");
				   MessageBody messageBody = new MessageBody("Meeting");
				   appointment.setBody(messageBody);
				   appointment.setLocation("Location");
		
				   appointment.setReminderMinutesBeforeStart(5);
				   AttendeeCollection attendeeCollection1 = appointment.getRequiredAttendees();

				   EmailAddress mailbox = new EmailAddress("ciby@vmokshaex.com");
				   Attendee attendee = new Attendee(mailbox);
				   attendeeCollection1.add(attendee); 
				   EmailAddress bookRoomAddress = new EmailAddress("room8@vmokshaex.com");
				   Mailbox sendInvitationsMode = new Mailbox(bookRoomAddress.getAddress());
				   FolderId destinationFolderId = new FolderId(WellKnownFolderName.Calendar, sendInvitationsMode);
			   
				   appointment.save(destinationFolderId, SendInvitationsMode.SendToAllAndSaveCopy);
				   Log.i("", "");
				   		   				   
			   }
			   catch(Exception e){
				
			   }
			return null;
		}
		
	}
	
	
	public class MyContacts extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			// Get the number of items in the contacts folder. To limit the size of the response, request only the TotalCount property.
			try {
//				ContactsFolder contactsfolder = ContactsFolder.bind(service, 
//				                                                    WellKnownFolderName.Contacts, 
//				                                                    new PropertySet(BasePropertySet.IdOnly, FolderSchema.TotalCount));
				int numItems = 10;//contactsfolder.getTotalCount();
				ItemView view = new ItemView(numItems);
				PropertySet propertySet = new PropertySet(BasePropertySet.IdOnly, ContactSchema.DisplayName);
				view.setPropertySet(propertySet);
				
				FindItemsResults<Item>  contactItems = service.findItems(WellKnownFolderName.Contacts, view);
				for(Item item : contactItems){
					
					Contact contact = (Contact) item;
					String contactName = contact.getDisplayName();
					
					
					Log.i("Manoj Behera", "Contact Name:...."+contactName);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public class UserAvailable extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   Date startDate,endDate;
			   
			   Date newDate = new Date();
				Date oldDate = new Date(new Date().getTime() + 30 * HOUR);
			try {
				List<AttendeeInfo> attendees = new ArrayList<AttendeeInfo>();
				
			    attendees.add(new AttendeeInfo("ciby@vmokshaex.com"));//, MeetingAttendeeType.Optional, true));//,new AttendeeInfo().setAttendeeType(MeetingAttendeeType.Optional)
//			    attendees.add(new AttendeeInfo("room2@vmex.com"));
			    
			    
			    
			    AvailabilityOptions availabilityOptions = new AvailabilityOptions();
			    availabilityOptions.setGoodSuggestionThreshold(49);
			    availabilityOptions.setMaximumNonWorkHoursSuggestionsPerDay(0);
			    availabilityOptions.setMaximumSuggestionsPerDay(2);
			    availabilityOptions.setMeetingDuration(30);
			    availabilityOptions.setMinimumSuggestionQuality(SuggestionQuality.Good);
			    TimeWindow timeWindow = new TimeWindow(newDate, oldDate);
			    availabilityOptions.setDetailedSuggestionsWindow(timeWindow);     
			    availabilityOptions.setRequestedFreeBusyView(FreeBusyViewType.FreeBusy);
			    
			 
			    GetUserAvailabilityResults results = service.getUserAvailability(attendees, timeWindow, AvailabilityData.FreeBusyAndSuggestions, availabilityOptions);
			    Log.i("", "");
			    ServiceResponseCollection<AttendeeAvailability> collection = results.getAttendeesAvailability();
			    for(AttendeeAvailability attendeeAvailability:collection){
			    	Collection<CalendarEvent> calendarEvents = attendeeAvailability.getCalendarEvents();
			    	for(CalendarEvent calendarEvent : calendarEvents){
			    		CalendarEventDetails calendarEventDetails = calendarEvent.getDetails();
			    		
			    	}
			    }
			    Log.i("", "");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
			


			
			return null;
		}
		
	}
	
	public class RoomLists extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			try{
			// Return all the room lists in the organization.
			// This method call results in a GetRoomLists call to EWS.
				EmailAddressCollection myRoomLists = service.getRoomLists();
				for(EmailAddress emailAddress: myRoomLists){
					Log.i("Manoj Behera", "Office Name: "+emailAddress.getName()+" EmailId: " + emailAddress.getAddress());
					
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	}

}
