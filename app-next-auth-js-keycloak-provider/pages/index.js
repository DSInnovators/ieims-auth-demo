import Layout from '../components/layout'
import axios from "axios";
import { signIn, signOut, useSession,getSession } from 'next-auth/react'

export default function Page () {

    const { data: session, status } = useSession()

   /* useEffect(()=>{
        var data = JSON.stringify(session);
       // console.log('Data',session?.accessToken)
    })*/

    const BASE_URL_STUDENT = "http://localhost:8888/user/hello"
    const getUserData = async ()=>{
        console.log('Cookie created name : user ');

        const data = {name: session?.user.email,token : session.accessToken}

        const config = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' +  session.accessToken
            }
        }

        await axios.get(BASE_URL_STUDENT,config)
            .then(res => {
                console.log(res);
            })
            .catch(error => {
                    alert('You are not authorize to view the content')
                    console.log("Error occured " + error)
                }
            )
    }

    const getAdminData = async ()=>{
        console.log('Cookie created name : user ');

        const data = {name: session?.user.email,token : session.accessToken}

        const config = {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' +  session.accessToken
            }
        }

        await axios.get('http://localhost:8888/admin/hello',config)
            .then(res => {
                console.log(res);
            })
            .catch(error => {
                    alert('You are not authorize to view the content')
                    console.log("Error occured " + error)
                }
            )
    }

  return (
      <Layout>
        <h1>NextAuth.js Example</h1>
        <p>
          This is the index page. The is where we start our coding!
        </p>
          {!session &&
          <>

              <h2>Student sign in</h2>
              {/*<br/>
      <button onClick={() => signIn()}>Sign in</button>*/}
          </>

          }
          {session &&

          <>
              Signed in as {session.user.email}

              <br/>

              User Name = {session.user.name}

              <br/>



              <button  onClick={getUserData}>Get user data</button>
              <button  onClick={getAdminData}>Get admin data</button>
          </>}

      </Layout>
  )
}