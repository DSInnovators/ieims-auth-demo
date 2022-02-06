import Link from "next/link"
import styles from "./footer.module.css"
import packageJSON from "../package.json"

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <hr />
      <ul className={styles.navItems}>

        <li className={styles.navItem}>
          <a href="http://dsinnovators.com/">Dynamic Solution Innovators</a>
        </li>


        <li className={styles.navItem}>
          <em>Implemented with next-auth@{packageJSON.dependencies["next-auth"]}</em>
        </li>
      </ul>
    </footer>
  )
}
