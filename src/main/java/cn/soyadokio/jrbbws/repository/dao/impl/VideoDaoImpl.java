package cn.soyadokio.jrbbws.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.soyadokio.jrbbws.domain.po.VideoPo;
import cn.soyadokio.jrbbws.repository.dao.VideoDao;
import cn.soyadokio.jrbbws.repository.dao.common.DbFactory;
import cn.soyadokio.jrbbws.utils.StringUtils;

/**
 * DAO层-接口实现
 * @author SoyaDokio
 * @date   2020-05-05
 */
@Service("videoDao")
public class VideoDaoImpl implements VideoDao {

	private static final Logger logger = LoggerFactory.getLogger(VideoDaoImpl.class);

	@Override
	public VideoPo queryVideo(String datestamp) {
		String sql = "select status,errMsg,datestamp,title,poster,url from Videos where datestamp=?;";
		logger.debug("(SQLLOG){}", sql);
		VideoPo resultVideoPo = new VideoPo();
		try (Connection conn = DbFactory.getConnection(); PreparedStatement pstat = conn.prepareStatement(sql);) {
			pstat.setString(1, datestamp);
			try (ResultSet rset = pstat.executeQuery();) {
				VideoPo videoPo = new VideoPo();
				if (rset.next()) {
					videoPo.setStatus(rset.getInt(1));
					videoPo.setErrMsg(rset.getString(2));
					videoPo.setDatestamp(rset.getString(3));
					videoPo.setTitle(rset.getString(4));
					videoPo.setPoster(rset.getString(5));
					videoPo.setUrl(rset.getString(6));
				}
				resultVideoPo = videoPo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultVideoPo;
	}

	@Override
	public List<VideoPo> queryVideos(String datestampFrom, String datestampTo) {
		StringBuffer sql = new StringBuffer("select status,errMsg,datestamp,title,poster,url from Videos where ");
		if (StringUtils.isDate(datestampFrom)) {
			sql.append("datestamp>='");
			sql.append(datestampFrom);
			sql.append("'");
		}
		if (StringUtils.isDate(datestampTo)) {
			if (sql.lastIndexOf("=") != -1) {
				sql.append(" and ");
			}
			sql.append("datestamp<='");
			sql.append(datestampTo);
			sql.append("'");
		}
		sql.append(";");
		if (sql.indexOf("=") == -1) {
			sql.replace(60, 65, "");
		}
		logger.debug("(SQLLOG){}", sql.toString());

		List<VideoPo> videoPos = new ArrayList<VideoPo>();
		try (Connection conn = DbFactory.getConnection();
				PreparedStatement pstat = conn.prepareStatement(sql.toString());) {
			try (ResultSet rset = pstat.executeQuery();) {
				while (rset.next()) {
					VideoPo videoPo = new VideoPo();
					videoPo.setStatus(rset.getInt(1));
					videoPo.setErrMsg(rset.getString(2));
					videoPo.setDatestamp(rset.getString(3));
					videoPo.setTitle(rset.getString(4));
					videoPo.setPoster(rset.getString(5));
					videoPo.setUrl(rset.getString(6));
					videoPos.add(videoPo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return videoPos;
	}

	@Override
	public int addVideo(VideoPo videoPo) {
		String sql = "insert into Videos (status,errMsg,datestamp,title,poster,url) values (?,?,?,?,?,?);";
		logger.debug("(SQLLOG){}", sql);
		int rows = 0;
		try (Connection conn = DbFactory.getConnection();) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstat = conn.prepareStatement(sql);) {
				pstat.setInt(1, videoPo.getStatus());
				pstat.setString(2, videoPo.getErrMsg());
				pstat.setString(3, videoPo.getDatestamp());
				pstat.setString(4, videoPo.getTitle());
				pstat.setString(5, videoPo.getPoster());
				pstat.setString(6, videoPo.getUrl());
				rows = pstat.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
			}
			if (rows != 1) {
				conn.rollback();
			} else {
				conn.commit();
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public int addVideos(List<VideoPo> videoPos) {
		String sql = "insert into Videos (status,errMsg,datestamp,title,poster,url) values (?,?,?,?,?,?);";
		logger.debug("(SQLLOG){}", sql);
		int rows = 0;
		try (Connection conn = DbFactory.getConnection();) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstat = conn.prepareStatement(sql);) {
				for (VideoPo videoPo : videoPos) {
					pstat.setInt(1, videoPo.getStatus());
					pstat.setString(2, videoPo.getErrMsg());
					pstat.setString(3, videoPo.getDatestamp());
					pstat.setString(4, videoPo.getTitle());
					pstat.setString(5, videoPo.getPoster());
					pstat.setString(6, videoPo.getUrl());
					int row = pstat.executeUpdate();
					rows += row;
				}
			} catch (SQLException e) {
				conn.rollback();
			}
			if (rows != videoPos.size()) {
				conn.rollback();
			} else {
				conn.commit();
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

	@Override
	public int updateVideo(VideoPo videoPo) {
		VideoPo dbVideoPo = queryVideo(videoPo.getDatestamp());
		StringBuffer sql = new StringBuffer("update Videos set ");
		if (dbVideoPo.getStatus() != videoPo.getStatus()) {
			sql.append("status=" + videoPo.getStatus());
			sql.append(",");
		}
		if (!dbVideoPo.getErrMsg().equals(videoPo.getErrMsg())) {
			sql.append("errMsg='" + videoPo.getErrMsg() + "'");
			sql.append(",");
		}
		if (!dbVideoPo.getTitle().equals(videoPo.getTitle())) {
			sql.append("title='" + videoPo.getTitle() + "'");
			sql.append(",");
		}
		if (!dbVideoPo.getPoster().equals(videoPo.getPoster())) {
			sql.append("poster='" + videoPo.getPoster() + "'");
			sql.append(",");
		}
		if (!dbVideoPo.getUrl().equals(videoPo.getUrl())) {
			sql.append("url='" + videoPo.getUrl() + "'");
			sql.append(",");
		}
		if (sql.lastIndexOf(",") == -1) {
			return 0;
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" where datestamp=?;");
		logger.debug("(SQLLOG){}", sql.toString());

		int rows = 0;
		try (Connection conn = DbFactory.getConnection();) {
			conn.setAutoCommit(false);
			try (PreparedStatement pstat = conn.prepareStatement(sql.toString());) {
				pstat.setString(1, videoPo.getDatestamp());
				rows = pstat.executeUpdate();
			} catch (SQLException e) {
				conn.rollback();
			}
			if (rows != 1) {
				conn.rollback();
			} else {
				conn.commit();
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rows;
	}

}
