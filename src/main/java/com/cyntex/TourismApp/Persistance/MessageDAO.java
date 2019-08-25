package com.cyntex.TourismApp.Persistance;

import java.sql.Types;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cyntex.TourismApp.Beans.ChatUserBean;
import com.cyntex.TourismApp.Beans.SendMessageQueryResponsBean;
import com.cyntex.TourismApp.Beans.ShopDetailsQueryResultBean;
import com.cyntex.TourismApp.Util.DataSourceManager;


@Component
public class MessageDAO {
	@Autowired
	private DataSourceManager dataSourceManager;
	

	private static final String saveMessageQuery=
			"insert into message_details(chat_group_id,username, message ,created_date) values (?,?,?,?)";
	private static final String getMessageDetailsQuery =
			"select * from message_details as one left join group_participant as two on (one.username = two.username and one.chat_group_id = two.chat_group_id ) where one.chat_group_id = ?  order by one.created_date ";
	
	
//	messageDAO.saveMessage(chatGroupId,username,message);

	
	
	
	@Transactional
	public void saveMessage(int chatGroupId, String username,  String message){
		dataSourceManager.getJdbcTemplate().update(saveMessageQuery,
                new Object[] {chatGroupId,username ,message, new Date()},
                new int[]{Types.INTEGER,Types.VARCHAR,Types.VARCHAR,Types.DATE});

		
	}
	
	@Transactional
	public List<SendMessageQueryResponsBean> getMessageDetails(int chat_group_id){
		 List<SendMessageQueryResponsBean> queryData = dataSourceManager.getJdbcTemplate().query(
				getMessageDetailsQuery,
                new Object[] {chat_group_id},
                new int[]{Types.INTEGER},
                (rs,rowNum) -> new SendMessageQueryResponsBean(
                        rs.getInt("message_id"),
                         rs.getString("message"),
                        rs.getDate("created_date"),
                        new ChatUserBean(rs.getString("username"),rs.getString("avatar")))
        );
        
		return queryData;
	}

	
}
