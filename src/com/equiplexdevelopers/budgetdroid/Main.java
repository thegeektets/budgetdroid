package com.equiplexdevelopers.budgetdroid;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.ClipData.Item;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
 
	
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	public static CharSequence mTitle;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
	}
    
	
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments

		FragmentManager fragmentManager = getSupportFragmentManager();
		if(position ==0){
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						DashboardFragment.newInstance(position + 1)).commit();
		}
		else if(position == 1){
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					TransactionsFragment.newInstance(position + 1)).commit();
	
		}
		else if(position == 2){
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					TargetsFragment.newInstance(position + 1)).commit();
	
		}
		else {
			fragmentManager
			.beginTransaction()
			.replace(R.id.container,
					SettingsFragment.newInstance(position + 1)).commit();
	
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		
		
		}
	}
    
	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class DashboardFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static DashboardFragment newInstance(int sectionNumber) {
			DashboardFragment fragment = new DashboardFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public DashboardFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = null;
			
			rootView = inflater.inflate(R.layout.fragment_main, container,
					false);		
        
			SimpleDateFormat sdf = new SimpleDateFormat("MMMM,yyyy");
			
			String month = sdf.format(new Date());
			
			TextView TMonth = (TextView) rootView.findViewById(R.id.month);
			
			TMonth.append(month);
			
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				
				int dmonth = cal.get(Calendar.MONTH);
				int dyear =  cal.get(Calendar.YEAR);
				
				DatabaseHandler db = new DatabaseHandler(getActivity());
				
				int income = db.getincomepermonth(dmonth, dyear);
				int expense = db.getexpensepermonth(dmonth, dyear);
				
				TextView Income = (TextView) rootView.findViewById(R.id.mincome);
				TextView Expense = (TextView) rootView.findViewById(R.id.mexpense);
				TextView Savings = (TextView) rootView.findViewById(R.id.msavings);
				
				Income.setText("Amount :"+income);
				Expense.setText("Amount :"+expense);
				Savings.setText("Amount :"+(income-expense));
	
			
			return rootView;
			
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((Main) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	public static class TransactionsFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		private EditText Amount;
		private EditText Tdate;
		private EditText Type;
		
		public static TransactionsFragment newInstance(int sectionNumber) {
			TransactionsFragment fragment = new TransactionsFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}
		
		
		public TransactionsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			rootView = inflater.inflate(R.layout.fragment_transaction, container,
					false);
			
			
			SimpleDateFormat format =  new SimpleDateFormat ("dd-MM-yyyy");
			
			String dte = format.format(new Date());
			
			Tdate= (EditText) rootView.findViewById(R.id.transactiondate);
			
			Tdate.setText(dte);
			
			Button save = (Button) rootView.findViewById(R.id.savetransaction);
			
			save.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Amount= (EditText) getView().findViewById(R.id.amount);    
					Tdate= (EditText) getView().findViewById(R.id.transactiondate);
					Type= (EditText) getView().findViewById(R.id.type);
		            		
					String amount = Amount.getText().toString();
					String tdate = Tdate.getText().toString();
					
					SimpleDateFormat format =  new SimpleDateFormat ("dd-MM-yyyy");
					Date datet = null;
					try {
						datet = format.parse(tdate);
						String type = Type.getText().toString();
						DatabaseHandler db = new DatabaseHandler(getActivity());
					    db.addTransaction(type , datet ,Integer.parseInt(amount));
						Toast.makeText(getActivity(), "transaction saved", Toast.LENGTH_SHORT).show();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			
					
					
					
				}
			});
			
			
			
			return rootView;
			
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((Main) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	public static class TargetsFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static TargetsFragment newInstance(int sectionNumber) {
			TargetsFragment fragment = new TargetsFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public TargetsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			
		    Log.v("Title", (String) mTitle);
		    
		    rootView = inflater.inflate(R.layout.fragment_targets, container,
					false);		
			return rootView;
			
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((Main) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	public static class SettingsFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static SettingsFragment newInstance(int sectionNumber) {
			SettingsFragment fragment = new SettingsFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public SettingsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = null;
			
		    rootView = inflater.inflate(R.layout.fragment_settings, container,
					false);		
			return rootView;
			
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((Main) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}
	
	
}
