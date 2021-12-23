import './AuthContainer.css'
import { get } from './api'
import PermissionError from './PermissionError'

function AuthContainer({ authenticated, token, refreshToken, profile, login, logout, keycloak }) {
  function sendAdminHello() {
    get(keycloak, 'http://localhost:8888/admin/hello')
      .then(function (text) {
        alert(text)
      })
      .catch(function (error) {
        if (error instanceof PermissionError) {
          alert(error.message)
        } else {
          console.log(error)
          alert('Network error')
        }
      })
  }

  function sendAdminUpHello() {
    get(keycloak, 'http://localhost:8888/admin/upHello')
      .then(function (text) {
        alert(text)
      })
      .catch(function (error) {
        if (error instanceof PermissionError) {
          alert(error.message)
        } else {
          console.log(error)
          alert('Network error')
        }
      })
  }

  function sendUserHello() {
    get(keycloak, 'http://localhost:8080/user/hello')
      .then(function (text) {
        alert(text)
      })
      .catch(function (error) {
        if (error instanceof PermissionError) {
          alert(error.message)
        } else {
          console.log(error)
          alert('Network error')
        }
      })
  }

  return (
    <main className="AuthContainer-main">
      <div className="AuthContainer-token">
        {
          authenticated ?
            <>
              <h3>Profile</h3>
              <pre className="AuthContainer-pre">
                <code>
                  {JSON.stringify(profile, null, 2)}
                </code>
              </pre>

              <h3>Token</h3>
              <pre className="AuthContainer-pre">
                  <code>
                    {JSON.stringify(token, null, 2)}
                  </code>
              </pre>

              <h3>Refresh Token</h3>
              <pre className="AuthContainer-pre">
                <code>
                  {JSON.stringify(refreshToken, null, 2)}
                </code>
              </pre>
            </>
            :
            <div>User is not authenticated</div>
        }
      </div>
      <div className="AuthContainer-action">
        {
          authenticated ?
            <>
              {
                <div>
                  <button className="AuthContainer-button" onClick={sendAdminHello}>Send ADMIN Hello</button>
                </div>
              }
              {
                keycloak.hasResourceRole('ADMIN', 'api-1') &&
                  keycloak.hasResourceRole('UP_ADMIN', 'api-2') &&
                  <div>
                    <button className="AuthContainer-button" onClick={sendAdminUpHello}>Send ADMIN Upstream Hello
                    </button>
                  </div>
              }
              {
                keycloak.hasResourceRole('USER', 'api-1') &&
                <div>
                  <button className="AuthContainer-button" onClick={sendUserHello}>Send USER Hello</button>
                </div>
              }
              <div>
                <button className="AuthContainer-button" onClick={logout}>Logout</button>
              </div>
            </>
            :
            <button className="AuthContainer-button" onClick={login}>Login</button>
        }
      </div>
    </main>
  )
}

export default AuthContainer
