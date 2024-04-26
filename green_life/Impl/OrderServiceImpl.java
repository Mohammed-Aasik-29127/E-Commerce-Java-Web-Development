package green_life.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import CartBean;
import OrderBean;
import OrderDetails;
import TransactionBean;
import OrderService;
import DBUtil;
import MailMessage;

public class OrderServiceImpl implements OrderService {

	@Override
	public String paymentSuccess(String userName, double paidAmount) {
		String status = "Order Placement Failed!";

		List<CartBean> cartItems = new ArrayList<CartBean>();
		cartItems = new CartServiceImpl().getAllCartItems(userName);

		if (cartItems.size() == 0)
			return status;

		TransactionBean transaction = new TransactionBean(userName, paidAmount);
		boolean ordered = false;

		String transactionId = transaction.getTransactionId();

		// System.out.println("Transaction: "+transaction.getTransactionId()+"
		// "+transaction.getTransAmount()+" "+transaction.getUserName()+"
		// "+transaction.getTransDateTime());

		for (CartBean item : cartItems) {

			double amount = new ProductServiceImpl().getProductPrice(item.getProdId()) * item.getQuantity();

			OrderBean order = new OrderBean(transactionId, item.getProdId(), item.getQuantity(), amount);

			ordered = addOrder(order);
			if (!ordered)
				break;
			else {
				ordered = new CartServiceImpl().removeAProduct(item.getUserId(), item.getProdId());
			}

			if (!ordered)
				break;
			else
				ordered = new ProductServiceImpl().sellNProduct(item.getProdId(), item.getQuantity());

			if (!ordered)
				break;
		}

		if (ordered) {
			ordered = new OrderServiceImpl().addTransaction(transaction);
			if (ordered) {

				MailMessage.transactionSuccess(userName, new UserServiceImpl().getFName(userName),
						transaction.getTransactionId(), transaction.getTransAmount());

				status = "Order Placed Successfully!";
			}
		}

		return status;
	}
	
	@Override
	public boolean addOrder(OrderBean order) {
		boolean flag = false;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into orders values(?,?,?,?,?)");

			ps.setString(1, order.getTransactionId());
			ps.setString(2, order.getProductId());
			ps.setInt(3, order.getQuantity());
			ps.setDouble(4, order.getAmount());
			ps.setInt(5, 0);

			int k = ps.executeUpdate();

			if (k > 0)
				flag = true;

		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	@Override
	public boolean addTransaction(TransactionBean transaction) {
		boolean flag = false;

		Connection con = DBUtil.provideConnection();

		PreparedStatement ps = null;

		try {
			ps = con.prepareStatement("insert into transactions values(?,?,?,?)");

			ps.setString(1, transaction.getTransactionId());
			ps.setString(2, transaction.getUserName());
			ps.setTimestamp(3, transaction.getTransDateTime());
			ps.setDouble(4, transaction.getTransAmount());

			int k = ps.executeUpdate();

			if (k > 0)
				flag = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return flag;
	}

  
}
