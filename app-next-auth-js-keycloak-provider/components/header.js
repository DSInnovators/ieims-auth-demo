import Link from "next/link"
import { signIn, signOut, useSession } from "next-auth/react"
import styles from "./header.module.css"
import axios from "axios";

// The approach used in this component shows how to build a sign in and sign out
// component that works on pages which support both client and server side
// rendering, and avoids any flash incorrect content on initial page load.
export default function Header() {
  const { data: session, status } = useSession()
  const loading = status === "loading"

   //const {session, status } = useSession();

  const logOut = async () => {
    console.log('Logout')
    console.log(session);
    //const data = session.token.token;  
    //console.log(data);
    //signOut();

    const refresh_token = session.refreshToken;

    const logOutUrl = `${process.env.NEXT_PUBLIC_KEYCLOAK_ISSUER}`+'/protocol/openid-connect/logout';

    console.log(logOutUrl);
    console.log(`${process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID}`);
    console.log(refresh_token);

    const params = new URLSearchParams()
    params.append('client_id', `${process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID}`)
    params.append('client_secret', `${process.env.NEXT_PUBLIC_KEYCLOAK_SECRET}`)
    params.append('refresh_token', refresh_token)

    const config = {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    }

    await axios.post(logOutUrl, params, config)
        .then((result) => {
          console.log(result);
          signOut({ callbackUrl: `/` })
        })
        .catch((err) => {
          console.log(err);


          //signOut({ callbackUrl: `/` })
        })


  }

  return (
    <header>
      <noscript>
        <style>{`.nojs-show { opacity: 1; top: 0; }`}</style>
      </noscript>
      <div className={styles.signedInStatus}>
        <p
          className={`nojs-show ${
            !session && loading ? styles.loading : styles.loaded
          }`}
        >
          {!session && (
            <>
              <span className={styles.notSignedInText}>
                You are not signed in
              </span>
              <a

                className={styles.buttonPrimary}
                onClick={(e) => {
                  e.preventDefault()
                  signIn("keycloak")
                }}
              >
                Sign in
              </a>
            </>
          )}
          {session && (
            <>
              {session.user&& (
                <span
                  style={{ backgroundImage: `url('${session.user?.image}')` }}
                  className={styles.avatar}
                />
              )}
              <span className={styles.signedInText}>
                <small>Signed in as</small>
                <br />
                <strong>{session.user?.email || session.user?.name}</strong>
              </span>
              <a
                className={styles.button}
                onClick={logOut}
              >
                Sign out
              </a>
            </>
          )}
        </p>
      </div>
  {
    session && < nav >
    < ul
    className = {styles.navItems} >
        < li
    className = {styles.navItem} >
        < Link
    href = "/" >
        < a > Home < /a>
        < /Link>
        < /li>

        < li
    className = {styles.navItem} >
        < Link
    href = "/protected" >
        < a > Protected < /a>
        < /Link>
        < /li>
        < /ul>
        < /nav>
  }
    </header>
  )
}
