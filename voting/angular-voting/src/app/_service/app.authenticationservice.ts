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
        return this.httpClient.get("http://localhost:9088/search/user?email="+email);
    }

    isUserLoggedIn() {
        let user = sessionStorage.getItem('username')
        return !(user === null)
    }

    logOut() {
        sessionStorage.removeItem('userEmail');
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("role");
        sessionStorage.removeItem("userId");
        sessionStorage.removeItem("location");
    }
}