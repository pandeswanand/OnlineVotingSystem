import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../_service/app.authenticationservice';
import { Router } from '@angular/router';

@Component({
    selector: 'logout',
    template: ``
})
export class LogoutComponent implements OnInit {

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router) {

  }

  ngOnInit() {
    this.authenticationService.logOut();
    this.router.navigate(['/home']);
  }

}