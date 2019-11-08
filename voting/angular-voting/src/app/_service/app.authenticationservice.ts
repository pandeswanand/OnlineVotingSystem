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

    authenticate(email, password) {
        return this.httpClient.post<any>('http://localhost:9088/authenticate',{"emailId":email,"password":password});
    }
  
    register(data:any){
        return this.httpClient.post<any>("http://localhost:9088/register",data);
    }

    checkRole(email:string){
        alert("hello");
        return this.httpClient.get("http://localhost:9088/admin/search/user?email="+email);
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