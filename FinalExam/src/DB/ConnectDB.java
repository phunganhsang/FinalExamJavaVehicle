package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;

import Model.Vehicle;

public class ConnectDB {
	public static final String connectionURL = "jdbc:sqlserver://SANG\\SQLEXPRESS:1433;databaseName=FinalExam;user=sa;password=1234";
	
	public static Connection getDBConnect() {
		Connection con = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(connectionURL);
			return con;
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			System.out.println("Where is driver?");
			System.out.println(e.toString());
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		
		return null;
	}
	
	public int Insert(Vehicle v) {
		Connection conn = null;
		PreparedStatement sttm = null;
		try {
			String sql = "Insert into Vehicle ([Owner Name], [Identity Card], [Vehicle Type], [License Plate], [Brand], [Chassis Number], [Engine Number], [Registration Date]) values(?,?,?,?,?,?,?,?)";
			conn = getDBConnect();
			sttm = conn.prepareStatement(sql);
			sttm.setString(1, v.getOwnerName());
			sttm.setInt(2, v.getIdentityCard());
			sttm.setString(3, v.getType());
			sttm.setString(4, v.getLicensePlate());
			sttm.setString(5, v.getBrand());
			sttm.setString(6, v.getChassisNumber());
			sttm.setString(7, v.getEngineNumber());
			sttm.setDate(8, v.getDate());
			if(sttm.executeUpdate() > 0) {
				System.out.println("Insert thanh cong");
				return 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		} finally {
			try {
				sttm.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return -1;
	}
	
	
	public int Update(Vehicle v) {
		Connection conn = null;
		PreparedStatement sttm = null;
		try {
			String sql = "Update Vehicle set [Owner Name] = ?, [Identity Card] = ?, [Vehicle Type] = ?, [Brand] = ?, [Chassis Number] = ?, [Engine Number] = ?, [Registration Date] = ? where  [License Plate] = ?";
			conn = getDBConnect();
			sttm = conn.prepareStatement(sql);
			sttm.setString(1, v.getOwnerName());
			sttm.setInt(2, v.getIdentityCard());
			sttm.setString(3, v.getType());
			sttm.setString(4, v.getBrand());
			sttm.setString(5, v.getChassisNumber());
			sttm.setString(6, v.getEngineNumber());
			sttm.setDate(7, v.getDate());
			sttm.setString(8, v.getLicensePlate());
			if(sttm.executeUpdate() > 0) {
				System.out.println("Update thanh cong");
				return 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		} finally {
			try {
				sttm.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return -1;
	}
	
	
	public int Delete(String lp) {
		Connection conn = null;
		PreparedStatement sttm = null;
		try {
			String sql = "Delete Vehicle where [License Plate] = ?";
			conn = getDBConnect();
			sttm = conn.prepareStatement(sql);
			sttm.setString(1, lp);
			if(sttm.executeUpdate() > 0) {
				System.out.println("Delete thanh cong");
				return 1;
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				sttm.close();
				conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return -1;
	}
	
	public int SignIn(String userName, String password, String role) {
		int check=-1;
		Connection conn = null;
		PreparedStatement sttm = null;
		ResultSet rs = null;
		try {
			String sql = "Select * From Account Where username = ? and password = ? and role = ? ";
			conn = getDBConnect();
			sttm = conn.prepareStatement(sql);
			sttm.setString(1, userName);
			sttm.setString(2, password);
			sttm.setString(3, role);
			rs = sttm.executeQuery();
			if(rs.next())
				check=1;
			else
				check=-1;
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				sttm.close();
				conn.close();
				rs.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return check;
	}
	
	public List<Vehicle> getAllVehicle(){
		List<Vehicle> list = new ArrayList<>();
		Connection conn = null;
		Statement sttm = null;
		ResultSet rs = null;
		try {
			String sql = "Select [Owner Name], [Identity Card], [Vehicle Type], [License Plate], [Brand], [Chassis Number], [Engine Number], [Registration Date] From Vehicle";
			conn = getDBConnect();
			sttm = conn.createStatement();
			rs = sttm.executeQuery(sql);
			while(rs.next()) {
				Vehicle v = new Vehicle();
				v.setOwnerName(rs.getString(1));
				v.setIdentityCard(rs.getInt(2));
				v.setType(rs.getString(3));
				v.setLicensePlate(rs.getString(4));
				v.setBrand(rs.getString(5));
				v.setChassisNumber(rs.getString(6));
				v.setEngineNumber(rs.getString(7));
				v.setDate(rs.getDate(8));
				list.add(v);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				sttm.close();
				conn.close();
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return list;
	}
	
	public Vehicle getVehicleByLp(String lp) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement sttm = null;
		String sql = "Select [Owner Name],[Identity Card],[Vehicle Type],[License Plate],[Brand],[Chassis Number],[Engine Number], [Registration Date] from Vehicle where [License Plate] = ?";
		Vehicle v = new Vehicle();
		try {
			conn = getDBConnect();
			sttm = conn.prepareStatement(sql);
			sttm.setString(1, lp);
			rs = sttm.executeQuery();
			while(rs.next()) {
				v.setOwnerName(rs.getString(1));
				v.setIdentityCard(rs.getInt(2));
				v.setType(rs.getString(3));
				v.setLicensePlate(rs.getString(4));
				v.setBrand(rs.getString(5));
				v.setChassisNumber(rs.getString(6));
				v.setEngineNumber(rs.getString(7));
				v.setDate(rs.getDate(8));
				return v;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
		} finally {
			try {
				sttm.close();
				conn.close();
				rs.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return null;
	}
	
	public List<Vehicle> getVehicleByOwnerName(String name){
		List<Vehicle> list = new ArrayList<>();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement sttm = null;

		try {
			String sql = "Select [Owner Name], [Identity Card], [Vehicle Type], [License Plate], [Brand], [Chassis Number], [Engine Number], [Registration Date] From Vehicle where [Owner Name] like '%"+name+"%'";
			conn = getDBConnect();
			sttm = conn.prepareStatement(sql);
			rs = sttm.executeQuery();
			
			while(rs.next()) {
				Vehicle v = new Vehicle();
				v.setOwnerName(rs.getString(1));
				v.setIdentityCard(rs.getInt(2));
				v.setType(rs.getString(3));
				v.setLicensePlate(rs.getString(4));
				v.setBrand(rs.getString(5));
				v.setChassisNumber(rs.getString(6));
				v.setEngineNumber(rs.getString(7));
				v.setDate(rs.getDate(8));
				list.add(v);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				rs.close();
				//SQL_D.getCon().close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		//ConnectDB q = new ConnectDB();
		//Vehicle v =new Vehicle("sangupdate",123,"adasd","123","123","123","123");
		//q.Insert(v);
		//q.Update(v);
		//q.Delete("2245");
		//System.out.println(q.getAllVehicle());
		//System.out.println(q.getVehicleByLp("3333"));
	}
	
}
