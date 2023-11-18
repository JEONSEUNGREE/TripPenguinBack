package com.trip.penguin.room.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.penguin.company.domain.CompanyMS;
import com.trip.penguin.room.domain.RoomMS;
import com.trip.penguin.room.domain.RoomPicMS;
import com.trip.penguin.room.repository.RoomMSRepository;
import com.trip.penguin.room.repository.RoomPicMsRepository;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class RoomServiceImpl implements RoomService {

	private RoomMSRepository roomMSRepository;

	private RoomPicMsRepository roomPicMsRepository;

	private RoomPicService roomPicService;

	@Autowired
	public RoomServiceImpl(RoomMSRepository roomMSRepository, RoomPicMsRepository roomPicMsRepository,
		RoomPicService roomPicService) {
		this.roomMSRepository = roomMSRepository;
		this.roomPicMsRepository = roomPicMsRepository;
		this.roomPicService = roomPicService;
	}

	@Override
	public RoomMS createRoom(RoomMS roomMS, CompanyMS companyMS, List<RoomPicMS> roomPicMSList) {

		roomMS.createRoomMs();

		/* 객실에 해당 되는 회사 생성 */
		roomMS.setCompanyInfo(companyMS);
		RoomMS createdRoom = roomMSRepository.save(roomMS);

		/* 객실 이미지 생성 없는 경우 기본 이미지 생성 */
		roomPicService.createRoomPics(roomMS, roomPicMSList);
		return createdRoom;
	}

	@Override
	public Optional<RoomMS> getRoomById(RoomMS roomMS) {
		return roomMSRepository.findById(roomMS.getId());
	}

	@Override
	public RoomMS updateRoom(RoomMS roomMS) {
		return roomMSRepository.save(roomMS);
	}

	@Override
	public List<RoomMS> getRoomPicListByRoomId(RoomMS roomMS) {
		return null;
	}

	@Override
	public List<RoomMS> getReviewListByRoomId(RoomMS roomMS) {
		return roomMSRepository.getReviewListByRoomId(roomMS.getId());
	}

	@Override
	public Optional<RoomMS> getRoomBookingList(RoomMS roomMS) {
		return null;
	}

	@Override
	public void deleteRoom(RoomMS roomMS) {
		roomMSRepository.deleteById(roomMS.getId());
	}

}