import { Component, OnInit} from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'admin',
    templateUrl: '../_html/app.404page.html'
})

export class Error404Component{
    constructor(private router:Router) {           
    }

    redirect(){
        if(sessionStorage.getItem("role")!= null){
            if(sessionStorage.getItem("role") == "user"){
                this.router.navigate(['/user']);
            }
            else{
                this.router.navigate(['/admin']);
            }
        }
        else{
            this.router.navigate(['/home']);
        }
    }

}