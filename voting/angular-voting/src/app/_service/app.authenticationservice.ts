import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

export class User{
    
    constructor(public status:string) {}
  
}

export class JwtResponse{
    constructor(public jwttoken:string) {}
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private httpClient:HttpClient, private router:Router) {}

    authenticate(username, password) {
        return this.httpClient.post<any>('http://13.126.85.104:9088/authenticate',{username,password})
    }
  
    register(name,password){
        return this.httpClient.post<any>("http://13.126.85.104:9088/register",{"username":name,"password":password,"isAdmin":"false","isDeleted":"false"}).subscribe((data)=>alert("Successfully Registered!"), error=>{alert(error.error);});
    }

    checkRole(name:string){
        return this.httpClient.get("http://13.126.85.104:9088/searchuser?name="+name);
    }

    isUserLoggedIn() {
        let user = sessionStorage.getItem('username')
        return !(user === null)
    }

    logOut() {
        sessionStorage.removeItem('username');
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("role");
        sessionStorage.removeItem("userId");
    }
}