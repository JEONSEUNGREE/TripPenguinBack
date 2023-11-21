package com.trip.penguin.recommand;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.trip.penguin.booking.domain.BookingMS;
import com.trip.penguin.booking.service.BookingMsService;
import com.trip.penguin.company.domain.CompanyMS;
import com.trip.penguin.company.service.CompanyService;
import com.trip.penguin.constant.CommonConstant;
import com.trip.penguin.recommand.room.dao.RoomRecDAO;
import com.trip.penguin.recommand.room.service.RoomRecService;
import com.trip.penguin.recommand.room.view.MainRecRoomSchCdt;
import com.trip.penguin.review.domain.ReviewMS;
import com.trip.penguin.review.service.ReviewService;
import com.trip.penguin.room.domain.RoomMS;
import com.trip.penguin.room.service.RoomService;
import com.trip.penguin.user.domain.UserMS;
import com.trip.penguin.user.service.UserService;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

// @DataJpaTest가 빈설정에서 오류 발생
// 해결이 안되서 일단 SpringBootTest로 진행
@ActiveProfiles("test")
@SpringBootTest
public class RecRoomDataTest {

	private final RoomService roomService;

	private final RoomRecService roomRecService;

	private final UserService userService;

	private final CompanyService companyService;

	private final ReviewService reviewService;

	private final BookingMsService bookingMsService;

	private CompanyMS beforeCommitCompany;

	private UserMS beforeCommitUser;

	private List<BookingMS> beforeCommitBookingMs = new ArrayList<>();

	private List<RoomMS> beforeCommitRoomMSList = new ArrayList<>();

	private List<ReviewMS> reviewList = new ArrayList<>();

	@Autowired
	public RecRoomDataTest(RoomService roomService, CompanyService companyService, ReviewService reviewService,
		BookingMsService bookingMsService, UserService userService, EntityManager em, RoomRecService roomRecService) {
		this.roomService = roomService;
		this.companyService = companyService;
		this.reviewService = reviewService;
		this.bookingMsService = bookingMsService;
		this.userService = userService;
		this.roomRecService = roomRecService;
	}

	@BeforeEach
	public void beforeData() {

		/* 회사 가입 */
		beforeCommitCompany = CompanyMS.builder()
			.com_nm("testNm")
			.comEmail("test@test.com")
			.comPwd("testPwd")
			.comImg("defaultImg")
			.comAddress("location")
			.comApproval(CommonConstant.N.name())
			.userRole("ROLE_COM")
			.build();

		/* 객실 등록 */
		for (int i = 1; i < 4; i++) {
			beforeCommitRoomMSList.add(RoomMS.builder()
				.roomNm("testNm" + i)
				.checkIn(LocalDateTime.now())
				.checkOut(LocalDateTime.now())
				.couponYn(CommonConstant.Y.name())
				.thumbNail("default")
				.sellPrc(120000)
				.maxCount(5)
				.soldOutYn(CommonConstant.N.name())
				.roomDesc("Desc")
				.build());
		}

		/* 회원 가입 정보 */
		beforeCommitUser = UserMS.builder()
			.offYn("N")
			.userCity("Seoul")
			.userImg("default")
			.userEmail("test@email.com")
			.userRole("user")
			.userNick("default")
			.userPwd("test")
			.userFirst("t")
			.userLast("est")
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		/* 예약 등록 */
		for (int i = 0; i < 3; i++) {
			beforeCommitBookingMs.add(BookingMS.builder()
				.room(beforeCommitRoomMSList.get(i))
				.bookNm("test" + i)
				.userMS(beforeCommitUser)
				.couponYn(CommonConstant.Y.name())
				.sellPrc(500000)
				.payMethod("payMethod")
				.checkIn(LocalDateTime.now())
				.checkOut(LocalDateTime.now())
				.payAmount(500000)
				.build());
		}

		/* 리뷰 4개 생성 */
		for (int i = 1; i <= 4; i++) {
			reviewList.add(ReviewMS.builder()
				.reTitle("title" + i)
				.reContent("content" + i)
				.rating(i)
				.reAccom("no reply" + i)
				.report(CommonConstant.N.name())
				.build());
		}
	}

	@Test
	@DisplayName("메인 화면 데이터 테스트")
	@Transactional
	void createRoomTest() {

		// given
		/* 유저 가입 */
		userService.signUpUser(beforeCommitUser);

		/* 회사 가입 */
		companyService.createCompany(beforeCommitCompany);

		/* 객실 생성 */
		roomService.createRoom(beforeCommitRoomMSList.get(0), beforeCommitCompany, new ArrayList<>());
		roomService.createRoom(beforeCommitRoomMSList.get(1), beforeCommitCompany, new ArrayList<>());
		roomService.createRoom(beforeCommitRoomMSList.get(2), beforeCommitCompany, new ArrayList<>());

		/* 예약 등록 */
		bookingMsService.createBookingMs(beforeCommitBookingMs.get(0), beforeCommitUser);
		bookingMsService.createBookingMs(beforeCommitBookingMs.get(1), beforeCommitUser);
		bookingMsService.createBookingMs(beforeCommitBookingMs.get(2), beforeCommitUser);

		/* 리뷰 2개씩 등록 */
		// 첫번째 객실에 리뷰 등록 2개 등록
		reviewService.createReviewMs(reviewList.get(0), beforeCommitBookingMs.get(0), beforeCommitUser);
		reviewService.createReviewMs(reviewList.get(1), beforeCommitBookingMs.get(0), beforeCommitUser);

		// 두번째 객실에 리뷰 등록 2개 등록
		reviewService.createReviewMs(reviewList.get(2), beforeCommitBookingMs.get(1), beforeCommitUser);
		reviewService.createReviewMs(reviewList.get(3), beforeCommitBookingMs.get(1), beforeCommitUser);

		// when
		/* 리뷰 목록 조회 */
		MainRecRoomSchCdt recRoomSchCdt = MainRecRoomSchCdt.builder()
			.pageNumber(0)
			.pageSize(3)
			.build();

		List<RoomRecDAO> mainRecRoomList = roomRecService.getMainRecRoomListWithPaging(recRoomSchCdt);

		// then
		assertEquals(mainRecRoomList.size(), 3);
		// assertEquals(mainRecRoomList.get(0).getRatingAvg(), 1.5D);
		// assertEquals(mainRecRoomList.get(1).getRatingAvg(), 3.5D);
		// assertNull(mainRecRoomList.get(2).getRatingAvg());

	}

}
