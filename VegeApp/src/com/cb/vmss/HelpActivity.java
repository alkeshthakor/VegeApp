package com.cb.vmss;

import java.util.ArrayList;
import java.util.List;

import com.cb.vmss.adapter.FAQExpandableAdapter;
import com.cb.vmss.model.FAQBean;
import com.cb.vmss.util.Constant;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class HelpActivity extends ActionBarActivity {

	private Toolbar toolbar;
	private ExpandableListView mFaqListView;
	private List<FAQBean> mFAGList;
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		mFaqListView.setIndicatorBounds(mFaqListView.getRight()- 50, mFaqListView.getWidth());
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
			mTitle.setText(getResources().getString(R.string.lbl_help));

			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
			upArrow.setColorFilter(getResources().getColor(android.R.color.black), Mode.SRC_ATOP);
			getSupportActionBar().setHomeAsUpIndicator(upArrow);

		}
		
		
		mFaqListView=(ExpandableListView)findViewById(R.id.faq_list);
		
		mFAGList=createFAGList();
		
		FAQExpandableAdapter mAdapter=new FAQExpandableAdapter(getApplicationContext(), mFAGList);
		mFaqListView.setAdapter(mAdapter);
	
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			setResult(Constant.CODE_BACK);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private List<FAQBean> createFAGList(){
		
		mFAGList=new ArrayList<FAQBean>();
		
		FAQBean faq1=new FAQBean();
		faq1.setQuestion("What is Sabzi@door?");
		faq1.setAnswer("Sabzi@door is fastest retail chain Ahmedabad based local company which helps you to order all kinds or fruits and vegetables that you need daily. Check out our mobile application at Sabziatdoor.com/download or visit our website www.sabziatdoor.com. You have an option to order also by call or Whatsapp.");
		mFAGList.add(faq1);
		
		FAQBean faq2=new FAQBean();
		faq2.setQuestion("How do I order via Sabzi@door?");
		faq2.setAnswer("We made our application and website flow is pretty simple. Choose a preferred vegetables and fruits that will delivers to you. Confirm the order and check out the cart with everything you need and order will delivers to you within 120 minutes.");
		mFAGList.add(faq2);
		
		FAQBean faq3=new FAQBean();
		faq3.setQuestion("What kind of products do you sell?");
		faq3.setAnswer("We sell all kind of vegetables and fruits over 80 products spread across various categories.");
		mFAGList.add(faq3);
		
		FAQBean faq4=new FAQBean();
		faq4.setQuestion("Which cities and locations do you operate in?");
		faq4.setAnswer("Sabzi@door currently operates in Ahmedabad.");
		mFAGList.add(faq4);
		
		FAQBean faq5=new FAQBean();
		faq5.setQuestion("What is the minimum order value?");
		faq5.setAnswer("There is no minimum order value and there is no extra cost for shipping too.");
		mFAGList.add(faq5);
		
		FAQBean faq6=new FAQBean();
		faq6.setQuestion("How will I know if any item in my order is unavailable?");
		faq6.setAnswer("We try to make sure our inventory is always updated. In case an item you ordered is not available, we will deliver it to you on consecutive next day.");
		mFAGList.add(faq6);
		
		FAQBean faq7=new FAQBean();
		faq7.setQuestion("How can I make changes to my order before and after confirmation?");
		faq7.setAnswer("In case you want to change something in the order after it has been placed, you can write to us at support@sabziatdoor.com or simply call us at9925833511 between 9AM and 9PM.");
		mFAGList.add(faq7);
		
		FAQBean faq8=new FAQBean();
		faq8.setQuestion("Which locations do you deliver to?");
		faq8.setAnswer("Sabzi@door currently delivers to almost all areas in Ahmedabad. (Few area are charged nominal shipping amount)");
		mFAGList.add(faq8);
		
		FAQBean faq9=new FAQBean();
		faq9.setQuestion("Are there any delivery charges?");
		faq9.setAnswer("There are no extra delivery charges. (Few area are charged nominal shipping amount)");
		mFAGList.add(faq9);
		
		FAQBean faq10=new FAQBean();
		faq10.setQuestion("Can I change the delivery address of my order?");
		faq10.setAnswer("Just write us at support@Sabzi@door.com or contact us via call and we will promptly change the address as per your request.");
		mFAGList.add(faq10);
		
		FAQBean faq11=new FAQBean();
		faq11.setQuestion("Can I track the status of my order?");
		faq11.setAnswer("All the details of order including the present status are tractable via website account or mobile application.");
		mFAGList.add(faq11);
		
		FAQBean faq12=new FAQBean();
		faq12.setQuestion("What if I don’t receive my order in 120 minutes?");
		faq12.setAnswer("We try our best to deliver the order within 120 minutes. On rare occasions, due to unforeseen circumstances, your order might be delayed. In case of imminent delay, our customer support executive will keep you updated about the delivery time of your order.");
		mFAGList.add(faq12);
		
		FAQBean faq13=new FAQBean();
		faq13.setQuestion("What is the cancellation policy of Sabzi@door?");
		faq13.setAnswer("Sabzi@door provides easy and hassle free cancellation policy. You can cancel your order any time before the order is out for delivery. You can also reject the delivery at your doorstep or ask for replacement, if you are not satisfied with any product quality.");
		mFAGList.add(faq13);
		
		FAQBean faq14=new FAQBean();
		faq14.setQuestion("How do I cancel my order?");
		faq14.setAnswer("You can cancel the order from within the application inside Order History, or write to us at support@sabziatdoor.com or call us on 9925833511 to cancel your order.");
		mFAGList.add(faq14);
		
		FAQBean faq15=new FAQBean();
		faq15.setQuestion("What if the delivered product is defective?");
		faq15.setAnswer("We have a hassle free return and replacement policy for our customers. If you are not satisfied with the delivered product, ask our executive to replace it instantly.");
		mFAGList.add(faq15);
		
		FAQBean faq16=new FAQBean();
		faq16.setQuestion("What If I want to return something?");
		faq16.setAnswer("We understand the value of your every purchase. In case you are not satisfied with the delivered product, just return it to us or ask for the replacement without any cost.");
		mFAGList.add(faq16);
		
		FAQBean faq17=new FAQBean();
		faq17.setQuestion("What if I have any complaint regarding my order?");
		faq17.setAnswer("Complaints/feedback/Queries are always welcome. Drop us a line at support@sabziatdoor.com or give us a call at 9925833511 and we will be more than happy to help you.");
		mFAGList.add(faq17);
		
		FAQBean faq18=new FAQBean();
		faq18.setQuestion("How can I make payments at Sabzi@door?");
		faq18.setAnswer("Sabzi@door provides Cash on Delivery (COD) and online payment option is available.");
		mFAGList.add(faq18);
		
		FAQBean faq19=new FAQBean();
		faq19.setQuestion("What is the refund policy of Sabzi@door?");
		faq19.setAnswer("Shopping is hassle free and convenient on the Sabzi@door. Get in touch with our customer support in case you want to initiate the process of refund.");
		mFAGList.add(faq19);
		
		
		FAQBean faq20=new FAQBean();
		faq20.setQuestion("How can I download Sabzi@door Mobile App?");
		faq20.setAnswer("You can download our online mobile app from Google Play Store or App Store. Here is the link - bit.ly/grfrs");
		mFAGList.add(faq20);
		
		return mFAGList;
	}
}
