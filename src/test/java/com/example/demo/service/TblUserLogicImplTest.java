package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.example.demo.controllers.SearchController;
import com.example.demo.daos.TblUserDao;
import com.example.demo.entities.DisplayUser;
import com.example.demo.entities.SearchingInfo;
import com.example.demo.entities.TblUser;
import com.example.demo.logics.impl.TblUserLogicImpl;
import com.example.demo.utils.Common;

@RunWith(MockitoJUnitRunner.class)
public class TblUserLogicImplTest {
	@InjectMocks
	private TblUserLogicImpl sut;
	@Mock
	private TblUserDao tblUserDao;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() {
		List<TblUser> tblUsers = new ArrayList<>();
		tblUsers.add(new TblUser());

		List<DisplayUser> displayUsers = new ArrayList<>();
		displayUsers.add(new DisplayUser(1, "Tran Viet Hung", "01", "1989-12-20", "0123456789", "2016-12-25",
				"2017-12-25", "Ha Noi"));
		displayUsers.add(new DisplayUser(20, "Tran Đuc Cong", "01", "1995-01-11", "4567891235", "2017-01-17",
				"2017-01-31", "Ha Noi"));
		when(tblUserDao.findByUserNameAndUserPassword(anyString(), anyString())).thenReturn(tblUsers);
		when(tblUserDao.getNumberOfUsers(anyObject())).thenReturn((long) 24);
		when(tblUserDao.getListUsers(anyObject(), anyInt(), anyInt())).thenReturn(displayUsers);
	}

	/**
	 * [IN]
	 * LoginName:admin
	 * Password: admin
	 * [OUT]
	 * actual = 1
	 */
	@Test
	public void LoginByUsernameAndPasswordTest() {
		List<TblUser> tblUsers = sut.LoginByUsernameAndPassword("admin", "admin");
		assertEquals(1, tblUsers.size());

	}
	/**
	 * [IN]
	 * currentPage:1
	 * records/Page: 1
	 * totalRecords:4
	 * [OUT]
	 * actual = [1,2,3,4]
	 */
	@Test
	public void pagingTest() {
		ArrayList<Integer> pagingActual = Common.paging(1, 1, 4);
		ArrayList<Integer> pagingExpect = new ArrayList<>();
		pagingExpect.add(1);
		pagingExpect.add(2);
		pagingExpect.add(3);
		pagingExpect.add(4);
		pagingExpect.add(5);
		assertThat(pagingActual, is(pagingExpect));
	}

	/**
	 * [IN]
	 * searchingInfo: new SearchingInfo()
	 * [OUT]
	 * actual = 24
	 */
	@Test
	public void getNumberOfUsersTest() {
		SearchingInfo searchingInfo = new SearchingInfo();
		long totalRecords = sut.getNumberOfUsers(searchingInfo);
		assertEquals(24, totalRecords);

	}

	/**
	 * [IN]
	 * searchingInfo: defalut 
	 * currentPage:1
	 * recordsOfPage:2
	 * [OUT]
	 * actual : List<DisplayUser> displayUser :size =2
	 *     		displayUser 1: Username:Tran Viet Hung, Gender:Nam, Birthdate: 20/12/1989, InsuranceNumber:0123456789, StartDate:25/12/2016, EndDate:25/12/2017, PlaceOfRegister:Ha Noi 
	 *          displayUser 2: Username:Tran Đuc Cong, Gender:Nam, Birthdate: 11/01/1995, InsuranceNumber:4567891235, StartDate:17/01/2017, EndDate:31/01/2017, PlaceOfRegister:Ha Noi 
	 */
	@Test
	public void getListUsersTest() {
		SearchingInfo searchingInfo = new SearchingInfo();
		int currentPage = 1;
		int recordsOfPage = 2;
		ArrayList<DisplayUser> displayUsersActual = sut.getListUsers(searchingInfo, currentPage, recordsOfPage);
		ArrayList<DisplayUser> displayUsers = new ArrayList<>();

		DisplayUser displayUser = new DisplayUser();
		displayUser.setId(1);
		displayUser.setUsername("Tran Viet Hung");
		displayUser.setGender("Nam");
		displayUser.setBirthdate("20/12/1989");
		displayUser.setInsuranceNumber("0123456789");
		displayUser.setStartDate("25/12/2016");
		displayUser.setEndDate("25/12/2017");
		displayUser.setPlaceOfRegister("Ha Noi");

		displayUsers.add(displayUser);

		DisplayUser displayUser2 = new DisplayUser();
		displayUser2.setId(20);
		displayUser2.setUsername("Tran Đuc Cong");
		displayUser2.setGender("Nam");
		displayUser2.setBirthdate("11/01/1995");
		displayUser2.setInsuranceNumber("4567891235");
		displayUser2.setStartDate("17/01/2017");
		displayUser2.setEndDate("31/01/2017");
		displayUser2.setPlaceOfRegister("Ha Noi");

		displayUsers.add(displayUser2);

		assertThat(displayUsersActual, is(displayUsers));
	}
	/**
	 * Test formedCurrentPage 
	 * [In]
	 * currentPage:100
	 * recordsOgPage:1
	 * totalRecord:88
	 * [Out]
	 * currentPage:88
	 * currentPage is letter => return 1, currentPage < 1 =>  return 1, currentPage > totalPages => return totalPage
	 */
	@Test
	public void formedCurrentPageTest(){		
		int currentPageActual=Common.formedCurrentPage("100", 1, 88);
		assertEquals(87, currentPageActual);
	}
	
}
