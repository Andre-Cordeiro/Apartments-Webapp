import { useLocation, Link } from "react-router-dom"

export const PreviousPage = () => {
    
    const url = useLocation().pathname
    const newURL = url.substring(0, url.lastIndexOf('/'))

    return url === "/" ? 
        <div></div>
        :
        //<Link to={-1 as any}>
        <div style={{textAlign:"left", margin:"18px"}}>
            <Link to={newURL}>
                Back
            </Link>
        </div>
}