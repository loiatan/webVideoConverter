import com.agileoperations.webvideoconverter.Role
import com.agileoperations.webvideoconverter.User
import com.agileoperations.webvideoconverter.UserRole

class BootStrap {

    def init = { servletContext ->
		createDefaultUsersAndRoles()
    }
	
	def createDefaultUsersAndRoles() {
		def devOpsRole = Role.findByAuthority('ROLE_DEVOPS') ?: new Role(name: 'DevOps Administrator', authority: 'ROLE_DEVOPS').save(failOnError: true)

		def username = "admin"
		def devOpsUser = User.findByUsername(username) ?: new User(
				username: username,
				password: 'admin',
				enabled: true).save(failOnError: true)

		if (!devOpsUser.authorities.contains(devOpsRole)) {
			UserRole.create devOpsUser, devOpsRole
		}
	}
	
    def destroy = {
    }
}
