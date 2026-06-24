/**
 * Test Helpers — Shared utilities for Angular unit testing
 */
import { Type } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

/** Create component and return fixture + instance */
export function createComponent<T>(component: Type<T>): { fixture: ComponentFixture<T>; component: T } {
  const fixture = TestBed.createComponent(component);
  const component = fixture.componentInstance;
  fixture.detectChanges();
  return { fixture, component };
}

/** Find an element by CSS selector and data-testid */
export function findEl<T>(fixture: ComponentFixture<T>, testId: string): DebugElement {
  return fixture.debugElement.query(By.css(`[data-testid="${testId}"]`));
}

/** Find all elements by CSS selector and data-testid */
export function findAllEls<T>(fixture: ComponentFixture<T>, testId: string): DebugElement[] {
  return fixture.debugElement.queryAll(By.css(`[data-testid="${testId}"]`));
}

/** Set input value and dispatch input event */
export function setInputValue(fixture: ComponentFixture<unknown>, testId: string, value: string): void {
  const input = findEl(fixture, testId).nativeElement as HTMLInputElement;
  input.value = value;
  input.dispatchEvent(new Event('input'));
  fixture.detectChanges();
}

/** Click an element by data-testid */
export function clickEl<T>(fixture: ComponentFixture<T>, testId: string): void {
  findEl(fixture, testId).triggerEventHandler('click', null);
  fixture.detectChanges();
}

/** Wait for async operations */
export async function waitForAsync(ms = 100): Promise<void> {
  await new Promise(resolve => setTimeout(resolve, ms));
}

/** Mock ResizeObserver for components that use it */
export function mockResizeObserver(): void {
  window.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(), unobserve: jest.fn(), disconnect: jest.fn(),
  }));
}

/** Generate a mock File object */
export function createMockFile(name: string, size: number, type: string): File {
  const content = new ArrayBuffer(size);
  return new File([content], name, { type });
}
