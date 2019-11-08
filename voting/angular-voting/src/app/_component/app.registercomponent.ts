import { Component } from "@angular/core";
import { UserService } from "../_service/app.userservice";
import { AuthenticationService } from "../_service/app.authenticationservice";
import { User } from "../_model/app.user";
import { Address } from "../_model/app.address";
import { Router } from "@angular/router";

@Component({
    selector:'register',
    templateUrl:'../_html/app.register.html'
})
export class RegisterComponent{
    userdata:any={
        address:{}
    };
    constructor(private service:AuthenticationService, private router:Router){}

    register(){
        console.log(this.userdata);
        this.service.register(this.userdata).subscribe((data)=>{alert("Successfully Registered!");
            this.router.navigate(['/home']).then(()=>window.location.reload())}, error=>{alert(error.error);});
    }
}