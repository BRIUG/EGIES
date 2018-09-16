package casm.gis.action;

import casm.gis.domain.User;
import casm.gis.service.UserService;


/*
 * Create a user action
 * 2017-05-14 22:04:38
 */
public class UserAction extends BaseAction{

	private static final long serialVersionUID = -7767070495605287402L;

	private User user;
	
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String execute() {
		try {
			this.userService.addObject(this.user);
			
		} catch (Exception e) {
			e.printStackTrace();
			return INPUT;
		}
		return SUCCESS;
	}
}
