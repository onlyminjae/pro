package com.kosa.pro.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosa.pro.model.MasterCodeVO;
import com.kosa.pro.model.MemberVO;
import com.kosa.pro.model.ReviewBoardVO;
import com.kosa.pro.model.ReviewCommentVO;
import com.kosa.pro.model.search.ReviewSearchVO;
import com.kosa.pro.service.common.BaseService;

import lombok.extern.slf4j.Slf4j;
/** 후기 게시판 
 * @author kky
 *
 */
@Service
@Slf4j
public class ReviewBoardService extends BaseService {
	public Map<String, Object> reviewList(ReviewSearchVO search) throws Exception {
		Map<String, Object> map = new HashMap<>();

		map.put("reviewList", (List<ReviewBoardVO>) getDAO().selectBySearch("review.selectReviewList", search, "totalCount"));
		map.put("popularList", (List<ReviewBoardVO>) getDAO().selectList("review.selectPopularList", search));
		map.put("categoryList", (List<MasterCodeVO>)getDAO().selectList("code.selectCategoryCode", search));
		return map;
	}
	
	public MasterCodeVO codeName(ReviewSearchVO search) {
		return (MasterCodeVO) getDAO().selectOne("code.selectCodeName", search);
	}
	
	public Map<String, Object> reviewInfo(ReviewSearchVO search) {
		Map<String, Object> map = new HashMap<>();
		
		
		map.put("popularList", (List<ReviewBoardVO>) getDAO().selectList("review.selectPopularList", search));
		map.put("reviewInfo", getDAO().selectOne("review.selectOne", search));
		map.put("categoryList", (List<MasterCodeVO>)getDAO().selectList("code.selectCategoryCode", search));
		map.put("categoryName", (List<MasterCodeVO>)getDAO().selectList("code.selectCategoryCodeName", search));
		
		//댓글
		search.setRecordCount(5);
		map.put("commentList", (List<ReviewCommentVO>) getDAO().selectBySearch("comment.selectCommentList", search, "totalCount"));
		map.put("commentCount", getDAO().selectOne("comment.totalCount", search));
		
		return map;
	}
	
	public Map<String, Object> reviewWriteForm(ReviewSearchVO search) {
		Map<String, Object> map = new HashMap<>();
		map.put("categoryList", (List<MasterCodeVO>)getDAO().selectList("code.selectCategoryCode", search));
		
		return map;
	}
	
	public MemberVO reviewLoginUser(String memId) {
		return (MemberVO) getDAO().selectOne("member.selectMemId", memId);
	}
	
	@Transactional
	public int reviewInsert(ReviewBoardVO reviewBoard) {
		log.info("등록 전 게시판 시퀀스 = " + reviewBoard.getReviewSeq());
		getDAO().insert("review.merge", reviewBoard);
		log.info("등록 후 게시판 시퀀스 = " + reviewBoard.getReviewSeq());
		return reviewBoard.getReviewSeq();
	}
	@Transactional
	public void reviewViewCount(ReviewSearchVO search) {
		getDAO().update("review.updateViewCount", search);
		
	}
	@Transactional
	public void reviewCategorySave(ReviewSearchVO search) {
		getDAO().insert("review.insertCategory", search);
		getDAO().update("review.updateCategoryCount", search);
	}
	
	public List<ReviewCommentVO> commentScrollList(ReviewSearchVO search) {
		return (List<ReviewCommentVO>) getDAO().selectBySearch("comment.selectCommentList", search, "totalCount");
	}
	@Transactional
	public Map<String, Object> commentSave(ReviewCommentVO comment) {
		Map<String, Object> map = new HashMap<>();
		log.info("등록 전 댓글 시퀀스 = " + comment.getCommentSeq());
		getDAO().insert("comment.insert", comment);
		log.info("등록 후 댓글 시퀀스 = " + comment.getCommentSeq());
		
		map.put("comment", comment);
		map.put("count", getDAO().selectOne("comment.totalCount", comment.getReviewSeq()));
		
		return map;
	}
	
	
}
