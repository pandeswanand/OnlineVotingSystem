import { Component } from "@angular/core";
import { AuthenticationService } from "../_service/app.authenticationservice";
import { Router } from "@angular/router";
import { User } from "../_model/app.user";

@Component({
    selector:"home",
    templateUrl: "../_html/app.home.html"
})
export class Homecomponent {

    emailId:string;
    password:string;
    user:User;

    constructor(private service:AuthenticationService, private router:Router){}

    login(){
        alert(this.emailId);
        this.service.authenticate(this.emailId, this.password).subscribe(
            userData => {
                let tokenStr= 'Bearer '+ userData.token;
                sessionStorage.setItem('token', tokenStr);
                sessionStorage.setItem('userEmail',this.emailId);
                this.service.checkRole(this.emailId).subscribe((data:any)=>{this.user=data;this.checkRoles();});
            }, error=>{
                alert("Invalid Credentials");
                this.router.navigate(['/home']);
            }
      );
    }

    checkRoles(){
        if(this.user.isAdmin){
            sessionStorage.setItem('role',"admin");
            sessionStorage.setItem("userId",this.user.userId.toString());
            this.router.navigate(['/admin']).then(() => {
                window.location.reload();
            });
        
        }
        else{
            sessionStorage.setItem('role',"user");
            sessionStorage.setItem("userId",this.user.userId.toString());
            this.router.navigate(['/user']).then(() => {
                window.location.reload();
            });
        }
    }
}