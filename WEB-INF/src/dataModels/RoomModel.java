package dataModels;
public class RoomModel {
	
	int id;
	int bookingId;
	String status;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "RoomModel [id=" + id + ", bookingId=" + bookingId + ", status=" + status + "]";
	}
}