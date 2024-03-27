package it6022002_section;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import it6020002_objects.SectionObject;
import it6020002.ConnectionPoolImpl;
public class Section {
	private Connection con;
	private ConnectionPoolImpl cp;
	public Section() {
		this.cp = new ConnectionPoolImpl();
		try {
				this.con = this.cp.getConnection("Section");
				if(this.con.getAutoCommit())
				{
					this.con.setAutoCommit(false);
				}
		}
		catch(SQLException e)
		{
				e.printStackTrace();
		}
		
		
	}
	public ArrayList<SectionObject> getSectionObjects(SectionObject similar, byte total){
		ArrayList<SectionObject> items = new ArrayList<>();
		
		SectionObject item;
		
		String sql = "SELECT * FROM tblsection ";
		sql += "";
		sql += " ORDER BY section_name ASC ";
		sql += "LIMIT ?"; // limit x, y: x la noi bat dau, y la so luong can lay
		
		// bien dich cau lenh
	//	Statement sta = this.con.createStatement(); // rui ro, cham
		//sta.executeUpdate(sql);
		//CallableStatement cal = this.con.prepareCall(sql); 
		try {
			
			PreparedStatement pre = this.con.prepareStatement(sql); //khuyen dung
			//truyen so ban ghi can lay
			pre.setByte(1, total);
			
			//lay danh sach ban ghi
			ResultSet rs = pre.executeQuery();
			if (rs != null)
			{
				while(rs.next()) {
					item = new SectionObject();
					//item.setSection_id(rs.getShort(1));
					item.setSection_id(rs.getShort("section_id"));//khuyen dung cach nay
					item.setSection_name(rs.getString("section_name"));
					
					// dua vao tap hop
					items.add(item);
				}
			}
			// dong tap ket qua lai
			rs.close();
		}catch(SQLException e) {
			e.printStackTrace();
			try {
				this.con.rollback(); // tro ve trang thai an toan cua ket noi
			} catch(SQLException e1)
			{
				e1.printStackTrace();
			}
		}
		
		return items;
	}

	public boolean addSection(SectionObject item) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO tblsection(");
		sql.append("tblsection.section_name, tblsection.section_notes,tblsection.section_created_date,");
	    sql.append("tblsection.section_manager_id, tblsection.section_enable, tblsection.section_delete,");
	    sql.append("tblsection.section_last_modified, tblsection.section_created_author_id,");
	    sql.append("tblsection.section_name_en, tblsection.section_language)");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?);");
		


		// bien dich
		try {
			PreparedStatement pre = this.con.prepareStatement(sql.toString());
			// truyen gia tri
			pre.setString(1, item.getSection_name());
			pre.setString(2, item.getSection_notes());
			pre.setString(3, item.getSection_created_date());
			pre.setInt(4, item.getSection_manager_id());
			pre.setBoolean(5, item.isSection_enable());//getSection_enable()
			pre.setBoolean(6, item.isSection_delete());
			pre.setString(7, item.getSection_last_modified());
			pre.setInt(8, item.getSection_created_author_id());
			pre.setString(9, item.getSection_name_en());
			pre.setByte(10, item.getSection_language());
			int result = pre.executeUpdate();
			if(result == 0)// thuc thi
			{
				this.con.rollback();
				return false;
			}
			// xac nhan thuc thi sau cung
			this.con.commit();
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
			try {}
			catch(Exception er) {
				er.printStackTrace();
			}
		}
		return false;
	}
	public static void main(String[] args) {
		Section s = new Section();
		
		// tao chuyenmuc moi
		SectionObject nsection = new SectionObject();
		nsection.setSection_name("LAP trinh java nang cao");
		nsection.setSection_created_date("27/10/23");
		nsection.setSection_notes("Kiem thu chuc nang them chuyen muc");
		if(!s.addSection(nsection))
		{
			System.out.println("----------KHONG THANH CONG\n");
		}
		
		// lay danh sach chuyen muc
		ArrayList<SectionObject> items = s.getSectionObjects(null, (byte) 5);
		
		// in danh sach ra man hinh
		items.forEach(item -> {
			System.out.println(item);
		});
	}
}
